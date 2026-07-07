package aps
import csw.prefix.models.Prefix
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.params.stringKey
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val pitCommon = reusableScript {

    // =========================================================================
    // ICS.PIT.Sequencer COMMAND HANDLERS
    // ICD 21.2 Commands for ICS.PIT.Sequencer
    // =========================================================================

    // ICD 21.2.1.1 setup — sets up the PIT Loop: filter, pupil mask, and pupil
    // registration offsets from the most recent Pupil Registration Calibration Procedure.
    // pitFilter, pitPupilMask, pitToPshPrRotationOffset, pitToPshPrScaleOffset,
    // pitToPshPrXTranslationOffset, pitToPshPrYTranslationOffset: required
    // pshPointingOffsetsCalibrationImageCenterX/Y, pitPointingOffsetsCalibrationImageCenterX/Y,
    // aptPointingOffsetsCalibrationCentroidX/Y: optional
    // Completion Type: longRunning
    onSetup("setup") { command ->
        val pitFilter: String                              = command.kGet(stringKey("pitFilter"))!!.first
        val pitPupilMask: String                           = command.kGet(stringKey("pitPupilMask"))!!.first
        val pitToPshPrRotationOffset: Float                 = command.kGet(floatKey("pitToPshPrRotationOffset"))!!.first
        val pitToPshPrScaleOffset: Float                    = command.kGet(floatKey("pitToPshPrScaleOffset"))!!.first
        val pitToPshPrXTranslationOffset: Float             = command.kGet(floatKey("pitToPshPrXTranslationOffset"))!!.first
        val pitToPshPrYTranslationOffset: Float             = command.kGet(floatKey("pitToPshPrYTranslationOffset"))!!.first
        val pshPointingOffsetsCalibrationImageCenterX: Float? = command.kGet(floatKey("pshPointingOffsetsCalibrationImageCenterX"))?.first
        val pshPointingOffsetsCalibrationImageCenterY: Float? = command.kGet(floatKey("pshPointingOffsetsCalibrationImageCenterY"))?.first
        val pitPointingOffsetsCalibrationImageCenterX: Float? = command.kGet(floatKey("pitPointingOffsetsCalibrationImageCenterX"))?.first
        val pitPointingOffsetsCalibrationImageCenterY: Float? = command.kGet(floatKey("pitPointingOffsetsCalibrationImageCenterY"))?.first
        val aptPointingOffsetsCalibrationCentroidX: Float?  = command.kGet(floatKey("aptPointingOffsetsCalibrationCentroidX"))?.first
        val aptPointingOffsetsCalibrationCentroidY: Float?  = command.kGet(floatKey("aptPointingOffsetsCalibrationCentroidY"))?.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setup-start",
            helpKey   = "help.setup",
            messageId = "msg.setup.start"
        ))
        println("PitCommon: setup — pitFilter=$pitFilter, pitPupilMask=$pitPupilMask, " +
                "pitToPshPrRotationOffset=$pitToPshPrRotationOffset, pitToPshPrScaleOffset=$pitToPshPrScaleOffset, " +
                "pitToPshPrXTranslationOffset=$pitToPshPrXTranslationOffset, pitToPshPrYTranslationOffset=$pitToPshPrYTranslationOffset, " +
                "pshPointingOffsetsCalibrationImageCenterX=$pshPointingOffsetsCalibrationImageCenterX, " +
                "pshPointingOffsetsCalibrationImageCenterY=$pshPointingOffsetsCalibrationImageCenterY, " +
                "pitPointingOffsetsCalibrationImageCenterX=$pitPointingOffsetsCalibrationImageCenterX, " +
                "pitPointingOffsetsCalibrationImageCenterY=$pitPointingOffsetsCalibrationImageCenterY, " +
                "aptPointingOffsetsCalibrationCentroidX=$aptPointingOffsetsCalibrationCentroidX, " +
                "aptPointingOffsetsCalibrationCentroidY=$aptPointingOffsetsCalibrationCentroidY")
        // TODO: implement — selects the filter and saves all values in global sequencer script
        // variables to be used to take exposures or during loop execution computations
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setup-complete",
            helpKey   = "help.setup",
            messageId = "msg.setup.complete"
        ))
    }

    // ICD 21.2.1.2 startPitLoop — integrationTime: float (second), gainMode: enum (12-BIT, 16-BIT), both required
    // Completion Type: longRunning
    onSetup("startPitLoop") { command ->
        val integrationTime: Float = command.kGet(floatKey("integrationTime"))!!.first
        val gainMode: String       = command.kGet(stringKey("gainMode"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "startPitLoop-start",
            helpKey   = "help.startPitLoop",
            messageId = "msg.startPitLoop.start"
        ))
        println("PitCommon: startPitLoop — integrationTime=$integrationTime, gainMode=$gainMode")
        // TODO: implement — starts the PIT Loop
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "startPitLoop-complete",
            helpKey   = "help.startPitLoop",
            messageId = "msg.startPitLoop.complete"
        ))
    }

    // ICD 21.2.1.3 stopPitLoop — no parameters
    // Completion Type: immediate
    onSetup("stopPitLoop") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "stopPitLoop-start",
            helpKey   = "help.stopPitLoop",
            messageId = "msg.stopPitLoop.start"
        ))
        println("PitCommon: stopPitLoop — stopping the PIT Loop")
        // TODO: implement — stops the PIT Loop
        // Completion Type is immediate, so no simulated delay here unlike the longRunning handlers above
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "stopPitLoop-complete",
            helpKey   = "help.stopPitLoop",
            messageId = "msg.stopPitLoop.complete"
        ))
    }

}
