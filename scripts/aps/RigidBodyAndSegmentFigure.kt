package aps

import esw.ocs.dsl.core.reusableScript

val rigidBodyAndSegmentFigure = reusableScript {

    onSetup("calc-colorstep") { command ->
        println("RigidBodyAndSegmentFigure: received calc-colorstep")
    }
}
