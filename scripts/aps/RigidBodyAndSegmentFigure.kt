package aps

import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.core.ScriptScope
import csw.prefix.javadsl.JSubsystem
import kotlin.time.Duration.Companion.seconds

val rigidBodyAndSegmentFigure = reusableScript {

    val scriptScope: ScriptScope = this

    val glc = Assembly(JSubsystem.M1CS, "GLC", defaultTimeout = 60.seconds)

    onSetup("cmd-m1cs-moves") { command ->
        val response = scriptScope.sendToGlc(command)
        println("cmd-m1cs-moves response: $response")
    }

    onSetup("calc-colorstep") { command ->
        println("RigidBodyAndSegmentFigure: received calc-colorstep " + obsMode)

        if (obsMode.name() == "APS_software_only_mode") {
            println("got it")
        }
    }

    onSetup("calc-tt-offsets-to-acts") { command ->
        println("RigidBodyAndSegmentFigure: received calc-tt-offsets-to-acts")
    }

    onSetup("calc-decompose-acts") { command ->
        println("RigidBodyAndSegmentFigure: received calc-decompose-acts")
    }

}
