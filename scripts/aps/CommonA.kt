package aps
import csw.prefix.models.Prefix
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.params.stringKey
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val commonA = reusableScript {

    // =========================================================================
    // COMMON HANDLERS — shared across multiple procedures on Sequencer A
    // =========================================================================

    // ICD 30.2.1.1 — filter: enum required, pupilMask: enum required, analogGainMode: enum optional
    onSetup("alignmentProcedureStartup") { command ->
        val filter: String          = command.kGet(stringKey("filter"))!!.first
        val pupilMask: String       = command.kGet(stringKey("pupilMask"))!!.first
        val analogGainMode: String? = command.kGet(stringKey("analogGainMode"))?.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureStartup-start",
            helpKey   = "help.alignmentProcedureStartup",
            messageId = "msg.alignmentProcedureStartup.start"
        ))
        println("CommonA: alignmentProcedureStartup — filter=$filter, pupilMask=$pupilMask, analogGainMode=$analogGainMode")
        // TODO: implement — first step in on-sky procedures that do not need to acquire a new target
        delay(5.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "alignmentProcedureStartup-complete",
            helpKey   = "help.alignmentProcedureStartup",
            messageId = "msg.alignmentProcedureStartup.complete"
        ))
    }

    // ICD 30.2.1.2 — all parameters required except aptGainMode (optional)
    onSetup("alignmentProcedureNewStarStartup") { command ->
        val pitFilter: String                              = command.kGet(stringKey("pitFilter"))!!.first
        val pitPupilMask: String                          = command.kGet(stringKey("pitPupilMask"))!!.first
        val pitIntTime: Float                             = command.kGet(floatKey("pitIntTime"))!!.first
        val aptFilter: String                             = command.kGet(stringKey("aptFilter"))!!.first
        val aptIntTime: Float                             = command.kGet(floatKey("aptIntTime"))!!.first
        val aptGainMode: String?                          = command.kGet(stringKey("aptGainMode"))?.first
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
        println("CommonA: alignmentProcedureNewStarStartup — pitFilter=$pitFilter, pitPupilMask=$pitPupilMask, " +
                "pitIntTime=$pitIntTime, aptFilter=$aptFilter, aptIntTime=$aptIntTime, aptGainMode=$aptGainMode")
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
