package aps

import esw.ocs.dsl.core.script

script {

    loadScripts(rigidBodyAndSegmentFigure)

    onGoOnline {
        println("ApsSoftwareOnlyMode: sequencer going ONLINE")
    }

    onGoOffline {
        println("ApsSoftwareOnlyMode: sequencer going OFFLINE")
    }

    onAbortSequence {
        println("ApsSoftwareOnlyMode: sequence ABORTED")
    }

    onStop {
        println("ApsSoftwareOnlyMode: sequencer STOPPED")
    }

    onShutdown {
        println("ApsSoftwareOnlyMode: sequencer SHUTDOWN - cleaning up")
    }

    onGlobalError { error ->
        println("ApsSoftwareOnlyMode: unhandled error - ${error.reason}")
    }
}
