package aps
import csw.prefix.models.Prefix
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val commonD = reusableScript {

    // =========================================================================
    // COMMON HANDLERS — shared across multiple procedures on Sequencer D
    // Sender: APS.PEAS.AlignmentProcedureSequencerB (via serialized sequence)
    // =========================================================================

    // Takes a PSH exposure with the specified integration time
    // Parameter: time: Int
    onSetup("takeGoodExposure") { command ->
        val intTime: Float = command.kGet(floatKey("intTime"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "takeGoodExposure-start",
            helpKey   = "help.takeGoodExposure",
            messageId = "msg.takeGoodExposure.start"
        ))
        println("CommonD: takeGoodExposure — intTime=$intTime")
        // TODO: implement — take PSH exposure with the specified integration time
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "takeGoodExposure-complete",
            helpKey   = "help.takeGoodExposure",
            messageId = "msg.takeGoodExposure.complete"
        ))
    }

}
