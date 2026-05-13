package aps

import csw.params.commands.CommandResponse
import csw.params.commands.Setup
import csw.prefix.javadsl.JSubsystem
import esw.ocs.dsl.core.ScriptScope
import kotlinx.coroutines.delay
import csw.params.core.models.Id
import kotlin.time.Duration.Companion.seconds
import java.util.UUID

suspend fun ScriptScope.sendToGlc(command: Setup): CommandResponse.SubmitResponse {
    val glc = Assembly(JSubsystem.M1CS, "GLC", defaultTimeout = 60.seconds)
    return if (obsMode.name() == "aps-operational") {
        glc.submitAndWait(command)
    } else {
        println("sendToGlc: simulating (obsMode=${obsMode.name()}) ...")
        delay(5.seconds)
        println("sendToGlc: simulation complete")
        CommandResponse.Completed(Id(UUID.randomUUID().toString()))
    }
}

