package aps
import csw.prefix.models.Prefix

import csw.prefix.javadsl.JSubsystem
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.core.ScriptScope
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val rigidBodyAndSegmentFigureA = reusableScript {

    val scriptScope: ScriptScope = this

    onSetup("cmd-m1cs-moves") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "cmd-m1cs-moves-start",
            helpKey   = "help.cmd-m1cs-moves",
            messageId = "msg.cmd-m1cs-moves.start"
        ))
        val response = scriptScope.sendToGlc(command)
        println("cmd-m1cs-moves response: $response")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "cmd-m1cs-moves-complete",
            helpKey   = "help.cmd-m1cs-moves",
            messageId = "msg.cmd-m1cs-moves.complete"
        ))
    }

    onSetup("calc-colorstep") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-colorstep-start",
            helpKey   = "help.calc-colorstep",
            messageId = "msg.calc-colorstep.start"
        ))
        delay(2.seconds)
        println("RigidBodyAndSegmentFigureA: received calc-colorstep")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-colorstep-complete",
            helpKey   = "help.calc-colorstep",
            messageId = "msg.calc-colorstep.complete"
        ))
    }

    onSetup("calc-tt-offsets-to-acts") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-tt-offsets-start",
            helpKey   = "help.calc-tt-offsets-to-acts",
            messageId = "msg.calc-tt-offsets-to-acts.start"
        ))
        delay(2.seconds)
        println("RigidBodyAndSegmentFigureA: received calc-tt-offsets-to-acts")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-tt-offsets-complete",
            helpKey   = "help.calc-tt-offsets-to-acts",
            messageId = "msg.calc-tt-offsets-to-acts.complete"
        ))
    }

    onSetup("calc-decompose-acts") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-decompose-acts-start",
            helpKey   = "help.calc-decompose-acts",
            messageId = "msg.calc-decompose-acts.start"
        ))
        delay(2.seconds)
        println("RigidBodyAndSegmentFigureA: received calc-decompose-acts")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "calc-decompose-acts-complete",
            helpKey   = "help.calc-decompose-acts",
            messageId = "msg.calc-decompose-acts.complete"
        ))
    }

    onSetup("send-sequence-to-sequencerB") { command ->
        println("RigidBodyAndSegmentFigureA: received send-sequence-to-sequencerB")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "send-sequence-to-sequencerB-start",
            helpKey   = "help.send-sequence-to-sequencerB",
            messageId = "msg.send-sequence-to-sequencerB.start"
        ))

        val askUser    = Setup("APS.apsPeasSequencerA", "ask-user")
        val sequencerB = scriptScope.getPeasSequencer(SequencerLabel.A, SequencerLabel.B)

        println("RigidBodyAndSegmentFigureA: submitting ask-user sequence to sequencerB")
        val response = sequencerB.submitAndWait(
            sequenceOf(askUser),
            timeout = 120.seconds
        )
        println("RigidBodyAndSegmentFigureA: sequencerB response: $response")

        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "send-sequence-to-sequencerB-complete",
            helpKey   = "help.send-sequence-to-sequencerB",
            messageId = "msg.send-sequence-to-sequencerB.complete"
        ))
    }

}
