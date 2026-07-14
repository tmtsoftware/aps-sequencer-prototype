package aps
import csw.prefix.models.Prefix
import csw.params.commands.Sequence
import csw.params.core.models.Choice
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.core.ScriptScope
import esw.ocs.dsl.params.stringKey
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.choiceKey
import esw.ocs.dsl.params.choicesOf
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async

val commonA = reusableScript {

    val scriptScope: ScriptScope = this

    // =========================================================================
    // COMMON HANDLERS — shared across multiple procedures on Sequencer A
    // =========================================================================

    // ICD 30.2.1.1 — filter: enum required, pupilMask: enum required, analogGainMode: enum optional
    onSetup("alignmentProcedureStartup") { command ->
        val filter: Choice          = command.kGet(choiceKey("filter", choicesOf("F890N", "F891N", "F850M", "F750W", "F810N", "F630N", "F865N")))!!.first
        val pupilMask: Choice       = command.kGet(choiceKey("pupilMask", choicesOf("PH-2-0", "SH-0", "SH-2", "SH-5", "Clear")))!!.first
        val analogGainMode: Choice? = command.kGet(choiceKey("analogGainMode", choicesOf("LOW", "HIGH", "HDR")))?.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureStartup-start",
            helpKey   = "help.alignmentProcedureStartup",
            messageId = "msg.alignmentProcedureStartup.start"
        ))
        println("CommonA: alignmentProcedureStartup — filter=${filter.name()}, pupilMask=${pupilMask.name()}, analogGainMode=${analogGainMode?.name()}")

        // Step 1: set the APS Instrument Operating Mode to ON_SKY_MODE (ICS Sequencer).
        // sendToIcsSequencer applies the isSoftwareOnlyMode() facade internally.
        val setOperatingModeCmd = Setup(prefix, "setOperatingMode")
            .add(choiceKey("operatingMode", choicesOf("CALIBRATION_SOURCE_MODE", "STIMULUS_SOURCE_MODE", "ON_SKY_MODE", "STANDBY_MODE", "STARTUP_MODE")).set(Choice("ON_SKY_MODE")))
        val setOperatingModeResponse = scriptScope.sendToIcsSequencer(setOperatingModeCmd)
        println("alignmentProcedureStartup: setOperatingMode response=$setOperatingModeResponse")

        // Step 2: stop the PIT Loop before reconfiguring it (PIT Sequencer, facade-gated)
        val stopPitLoopCmd = Setup(prefix, "stopPitLoop")
        val stopPitLoopResponse = scriptScope.sendToPitSequencer(stopPitLoopCmd)
        println("alignmentProcedureStartup: stopPitLoop response=$stopPitLoopResponse")

        // Step 3: in parallel — set up the PIT Loop (PIT Sequencer, facade-gated) and set up
        // the PSH optical arm (PEAS Sequencer B, always sent for real — not facade-gated).
        // pitFilter reuses this command's filter directly (same enum). pitPupilMask, however,
        // maps down from this command's wider pupilMask enum (PH-2-0, SH-0, SH-2, SH-5, Clear)
        // to PIT.PupilMaskWheel's actual physical positions (PH-1-1, Clear) -- anything that
        // isn't "Clear" becomes "PH-1-1". pshFilter/pshPupilMask reuse filter/pupilMask
        // directly since PSH's enums match this command's own exactly.
        // TODO: pitToPshPr*Offset values should come from the most recent Pupil Registration
        // Calibration Procedure (peas-procedure-data-service) — placeholders (0.0f) until that's wired up.
        // TODO: procedureId/observationId placeholders — should come from the current procedure/session context.
        val pitPupilMaskValue = if (pupilMask.name() == "Clear") "Clear" else "PH-1-1"
        coroutineScope {
            val setupPitLoopDeferred = async {
                val setupPitLoopCmd = Setup(prefix, "setup")
                    .add(choiceKey("pitFilter", choicesOf("F890N", "F891N", "F850M", "F750W", "F810N", "F630N", "F865N")).set(filter))
                    .add(choiceKey("pitPupilMask", choicesOf("PH-1-1", "Clear")).set(Choice(pitPupilMaskValue)))
                    .add(floatKey("pitToPshPrRotationOffset").set(0.0f))
                    .add(floatKey("pitToPshPrScaleOffset").set(0.0f))
                    .add(floatKey("pitToPshPrXTranslationOffset").set(0.0f))
                    .add(floatKey("pitToPshPrYTranslationOffset").set(0.0f))
                scriptScope.sendToPitSequencer(setupPitLoopCmd)
            }
            val setupPshOpticalArmDeferred = async {
                val setupPshOpticalArmCmd = Setup(prefix, "setupPshOpticalArm")
                    .add(choiceKey("pshFilter", choicesOf("F890N", "F891N", "F850M", "F750W", "F810N", "F630N", "F865N")).set(filter))
                    .add(choiceKey("pshPupilMask", choicesOf("PH-2-0", "SH-0", "SH-2", "SH-5", "Clear")).set(pupilMask))
                    .add(stringKey("procedureId").set("TODO-procedureId"))
                    .add(stringKey("observationId").set("TODO-observationId"))
                val sequencerB = scriptScope.getPeasSequencer(SequencerLabel.A, SequencerLabel.B)
                sequencerB.submitAndWait(Sequence.create(listOf(setupPshOpticalArmCmd)))
            }
            val setupPitLoopResponse = setupPitLoopDeferred.await()
            val setupPshOpticalArmResponse = setupPshOpticalArmDeferred.await()
            println("alignmentProcedureStartup: setup(PIT) response=$setupPitLoopResponse")
            println("alignmentProcedureStartup: setupPshOpticalArm response=$setupPshOpticalArmResponse")
        }

        // Step 4: start the PIT Loop (PIT Sequencer, facade-gated)
        // TODO: integrationTime/gainMode placeholders — should come from procedure configuration.
        val startPitLoopCmd = Setup(prefix, "startPitLoop")
            .add(floatKey("integrationTime").set(1.0f))
            .add(choiceKey("gainMode", choicesOf("12-BIT", "16-BIT")).set(Choice("16-BIT")))
        val startPitLoopResponse = scriptScope.sendToPitSequencer(startPitLoopCmd)
        println("alignmentProcedureStartup: startPitLoop response=$startPitLoopResponse")

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureStartup-complete",
            helpKey   = "help.alignmentProcedureStartup",
            messageId = "msg.alignmentProcedureStartup.complete"
        ))
    }

    // ICD 30.2.1.2 — all parameters required except aptGainMode (optional)
    onSetup("alignmentProcedureNewStarStartup") { command ->
        val pitFilter: Choice                             = command.kGet(choiceKey("pitFilter", choicesOf("F890N", "F891N", "F850M", "F750W", "F810N", "F630N", "F865N")))!!.first
        val pitPupilMask: Choice                          = command.kGet(choiceKey("pitPupilMask", choicesOf("PH-2-0", "SH-0", "SH-2", "SH-5", "Clear")))!!.first
        val pitIntTime: Float                             = command.kGet(floatKey("pitIntTime"))!!.first
        val aptFilter: Choice                              = command.kGet(choiceKey("aptFilter", choicesOf("ND1", "ND2", "NB589", "OPEN")))!!.first
        val aptIntTime: Float                             = command.kGet(floatKey("aptIntTime"))!!.first
        val aptGainMode: Choice?                          = command.kGet(choiceKey("aptGainMode", choicesOf("12-BIT", "16-BIT")))?.first
        val pitToPshPrRotationOffset: Float               = command.kGet(floatKey("pitToPshPrRotationOffset"))!!.first
        val pitToPshPrScaleOffset: Float                  = command.kGet(floatKey("pitToPshPrScaleOffset"))!!.first
        val pitToPshPrXTranslationOffset: Float           = command.kGet(floatKey("pitToPshPrXTranslationOffset"))!!.first
        val pitToPshPrYTranslationOffset: Float           = command.kGet(floatKey("pitToPshPrYTranslationOffset"))!!.first
        val pshPointingOffsetsCalibrationImageCenterX: Float = command.kGet(floatKey("pshPointingOffsetsCalibrationImageCenterX"))!!.first
        val pshPointingOffsetsCalibrationImageCenterY: Float = command.kGet(floatKey("pshPointingOffsetsCalibrationImageCenterY"))!!.first
        val pitPointingOffsetsCalibrationImageCenterX: Float = command.kGet(floatKey("pitPointingOffsetsCalibrationImageCenterX"))!!.first
        val pitPointingOffsetsCalibrationImageCenterY: Float = command.kGet(floatKey("pitPointingOffsetsCalibrationImageCenterY"))!!.first
        val aptPointingOffsetsCalibrationCentroidX: Float = command.kGet(floatKey("aptPointingOffsetsCalibrationCentroidX"))!!.first
        val aptPointingOffsetsCalibrationCentroidY: Float = command.kGet(floatKey("aptPointingOffsetsCalibrationCentroidY"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureNewStarStartup-start",
            helpKey   = "help.alignmentProcedureNewStarStartup",
            messageId = "msg.alignmentProcedureNewStarStartup.start"
        ))
        println("CommonA: alignmentProcedureNewStarStartup — pitFilter=${pitFilter.name()}, pitPupilMask=${pitPupilMask.name()}, " +
                "pitIntTime=$pitIntTime, aptFilter=${aptFilter.name()}, aptIntTime=$aptIntTime, aptGainMode=${aptGainMode?.name()}")
        // TODO: implement — first step in on-sky procedures requiring acquisition of a new star target
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureNewStarStartup-complete",
            helpKey   = "help.alignmentProcedureNewStarStartup",
            messageId = "msg.alignmentProcedureNewStarStartup.complete"
        ))
    }

}
