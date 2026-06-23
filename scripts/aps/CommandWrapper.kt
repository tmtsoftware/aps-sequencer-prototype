package aps

import csw.params.commands.CommandResponse
import csw.params.commands.Sequence
import csw.params.commands.SequenceCommand
import csw.params.commands.Setup
import csw.params.core.formats.JavaJsonSupport
import csw.params.javadsl.JKeyType
import csw.prefix.javadsl.JSubsystem
import esw.ocs.api.models.ObsMode
import esw.ocs.dsl.core.ScriptScope
import esw.ocs.dsl.highlevel.RichSequencer
import kotlinx.coroutines.delay
import csw.params.core.models.Id
import kotlin.time.Duration.Companion.seconds
import java.util.ArrayList
import java.util.UUID

enum class SequencerLabel { A, B, C, D }

fun ScriptScope.getPeasSequencer(source: SequencerLabel, target: SequencerLabel): RichSequencer {
    val targetMode = obsMode.name().replace("Sequencer${source.name}", "Sequencer${target.name}")
    return Sequencer(JSubsystem.APS, ObsMode(targetMode), 120.seconds)
}

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

fun deserializeSequence(serializedSequence: String): Sequence {
    val jsArray = play.api.libs.json.Json.parse(serializedSequence) as play.api.libs.json.JsArray
    val jsValueList = scala.jdk.javaapi.CollectionConverters.asJava(jsArray.value())
    val commands = ArrayList<SequenceCommand>()
    for (jsValue in jsValueList) {
        commands.add(JavaJsonSupport.readSequenceCommand(jsValue))
    }
    return Sequence.create(commands)
}

fun getStringParam(command: Setup, keyName: String): String? =
    command.jGetStringMap()[keyName]