package aps
import csw.prefix.models.Prefix
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.core.ScriptScope
import esw.ocs.dsl.params.stringKey
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val commonB = reusableScript {

    val scriptScope: ScriptScope = this

    // =========================================================================
    // COMMON HANDLERS — shared across multiple procedures on Sequencer B
    // =========================================================================

    // Common handler — takes a PSH exposure and submits a sequence to Sequencer D
    // to find and identify centroids in that exposure.
    // Parameters: integrationTime: Float (seconds), d1SerializedSequence: String
    onSetup("takeGoodExposureAndFindCentroids") { command ->
        val integrationTime: Float    = command.kGet(floatKey("integrationTime"))!!.first
        val d1JsonStr: String         = command.kGet(stringKey("d1SerializedSequence"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "takeGoodExposureAndFindCentroids-start",
            helpKey   = "help.takeGoodExposureAndFindCentroids",
            messageId = "msg.takeGoodExposureAndFindCentroids.start"
        ))
        println("CommonB: takeGoodExposureAndFindCentroids — integrationTime=$integrationTime s")

        val sequencerD = scriptScope.getPeasSequencer(SequencerLabel.B, SequencerLabel.D)
        val seqD1 = aps.deserializeSequence(d1JsonStr)

        // TODO: implement — take PSH exposure with integrationTime, then submit D sequence
        delay(1.seconds)
        // to find and identify centroids in that exposure
        val responseD = sequencerD.submitAndWait(seqD1, timeout = 120.seconds)
        println("CommonB: takeGoodExposureAndFindCentroids — sequencerD response: $responseD")

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "takeGoodExposureAndFindCentroids-complete",
            helpKey   = "help.takeGoodExposureAndFindCentroids",
            messageId = "msg.takeGoodExposureAndFindCentroids.complete"
        ))
    }

    // ICD 31.2.1.7 — no parameters
    onSetup("calcImageAndPrOffsets") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calcImageAndPrOffsets-start",
            helpKey   = "help.calcImageAndPrOffsets",
            messageId = "msg.calcImageAndPrOffsets.start"
        ))
        println("CommonB: calcImageAndPrOffsets — calculating pupil registration offsets and image offset from reference center")
        // TODO: implement
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calcImageAndPrOffsets-complete",
            helpKey   = "help.calcImageAndPrOffsets",
            messageId = "msg.calcImageAndPrOffsets.complete"
        ))
    }

    // ICD 31.2.1.8 — no parameters
    onSetup("correctPitTracking") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "correctPitTracking-start",
            helpKey   = "help.correctPitTracking",
            messageId = "msg.correctPitTracking.start"
        ))
        println("CommonB: correctPitTracking — publishing pshPupilAndImageErrors event to PIT Loop")
        // TODO: implement — publish pshPupilAndImageErrors event containing PR and image offsets
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "correctPitTracking-complete",
            helpKey   = "help.correctPitTracking",
            messageId = "msg.correctPitTracking.complete"
        ))
    }

}
