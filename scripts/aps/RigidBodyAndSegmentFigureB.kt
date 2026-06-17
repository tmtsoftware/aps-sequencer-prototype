package aps
import csw.prefix.models.Prefix

import esw.ocs.dsl.core.reusableScript
import kotlinx.coroutines.CompletableDeferred

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
        // messageId is a stable, human-readable identifier for this prompt.
        // messageUuid (generated automatically inside buildProcedureEvent) is
        // what's actually unique per invocation - it's what lets the wait
        // below distinguish "this round's" response from a previous round's
        // stale leftover (CSW's Event Service delivers the last published
        // event on a key immediately upon subscribe).
        val promptMessageId = "msg.ask-user.prompt"
        val promptEvent = buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.USER_PROMPT,
            dialogKey = OriginatingPromptType.WARNING,
            helpKey   = "help.ask-user",
            messageId = promptMessageId
        )
        val promptMessageUuid = messageUuidOf(promptEvent)
            ?: throw IllegalStateException("RigidBodyAndSegmentFigureB: failed to read back messageUuid from the prompt event we just built")
        publishEvent(promptEvent)

        // Block until the UI publishes a userPromptResponseEvent whose
        // originatingMessageUuid matches the prompt above. For this phase,
        // the step handler proceeds regardless of decisionResponse /
        // errorResponse content once a matching response arrives; a later
        // phase will branch on those values.
        println("RigidBodyAndSegmentFigureB: waiting for userPromptResponseEvent matching $promptMessageUuid")
        val responseReceived = CompletableDeferred<Unit>()
        val responseEventKey = userPromptResponseEventKey(Prefix.apply(prefix)).toString()
        val subscription = onEvent(responseEventKey) { event ->
            if (event.isInvalid) return@onEvent
            val response = decodeUserPromptResponseEvent(event)
            if (response == null) return@onEvent
            if (response.originatingMessageUuid != promptMessageUuid) {
                println("RigidBodyAndSegmentFigureB: ignoring stale/non-matching userPromptResponseEvent: $response")
                return@onEvent
            }
            println("RigidBodyAndSegmentFigureB: received matching userPromptResponseEvent: $response")
            responseReceived.complete(Unit)
        }
        responseReceived.await()
        subscription.cancel()

        println("RigidBodyAndSegmentFigureB: ask-user complete")
        publishEvent(buildProcedureEvent(Prefix.apply(prefix), 
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "ask-user-complete",
            helpKey   = "help.ask-user",
            messageId = "msg.ask-user.complete"
        ))
    }

}
