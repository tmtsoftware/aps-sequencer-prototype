package aps
import csw.prefix.models.Prefix

import esw.ocs.dsl.core.reusableScript

val rigidBodyAndSegmentFigureB = reusableScript {

    onSetup("ask-user") { command ->

        println("RigidBodyAndSegmentFigureB: ask-user start")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "ask-user-start",
            helpKey   = "help.ask-user",
            messageId = "msg.ask-user.start"
        ))

        println("RigidBodyAndSegmentFigureB: received ask-user")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.USER_PROMPT,
            dialogKey = "ask-user-prompt",
            helpKey   = "help.ask-user",
            messageId = "msg.ask-user.prompt"
        ))
        println("RigidBodyAndSegmentFigureB: ask-user complete")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "ask-user-complete",
            helpKey   = "help.ask-user",
            messageId = "msg.ask-user.complete"
        ))
    }

}
