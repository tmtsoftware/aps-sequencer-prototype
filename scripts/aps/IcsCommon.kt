package aps
import csw.prefix.models.Prefix
import esw.ocs.dsl.core.reusableScript
import esw.ocs.dsl.params.stringKey
import esw.ocs.dsl.params.floatKey
import esw.ocs.dsl.params.kGet
import esw.ocs.dsl.params.first
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

val icsCommon = reusableScript {

    // =========================================================================
    // ICS.ICS.Sequencer COMMAND HANDLERS
    // ICD 16.2 Commands for ICS.ICS.Sequencer
    // =========================================================================

    // ICD 16.2.1.1 setOperatingMode — operatingMode: enum required
    // (CALIBRATION_SOURCE_MODE, STIMULUS_SOURCE_MODE, ON_SKY_MODE, STANDBY_MODE, STARTUP_MODE)
    // Completion Type: longRunning
    onSetup("setOperatingMode") { command ->
        val operatingMode: String = command.kGet(stringKey("operatingMode"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setOperatingMode-start",
            helpKey   = "help.setOperatingMode",
            messageId = "msg.setOperatingMode.start"
        ))
        println("IcsCommon: setOperatingMode — operatingMode=$operatingMode")
        // TODO: implement — sets the APS Instrument Operating Mode.
        // Per ICD 16.3, will eventually send some combination of downstream commands
        // depending on the target mode, e.g.: STIM.InsertionStage.selectSource,
        // STIM.FiberSourceStage.setSourceIntensity, FOC.CalibrationSourceStage.setSourceIntensity,
        // ABE.Shutter.commandShutter, ABE.Enclosure.commandPurgeAir, FOC.KMirror.setMode.
        delay(1.seconds)

        println("IcsCommon: setOperatingMode — operatingMode=$operatingMode complete")

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setOperatingMode-complete",
            helpKey   = "help.setOperatingMode",
            messageId = "msg.setOperatingMode.complete"
        ))
    }

    // ICD 16.2.1.2 setupFocForAcquisition — pitToPshPrOffset: float (degree), required
    // Completion Type: longRunning
    onSetup("setupFocForAcquisition") { command ->
        val pitToPshPrOffset: Float = command.kGet(floatKey("pitToPshPrOffset"))!!.first

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setupFocForAcquisition-start",
            helpKey   = "help.setupFocForAcquisition",
            messageId = "msg.setupFocForAcquisition.start"
        ))
        println("IcsCommon: setupFocForAcquisition — pitToPshPrOffset=$pitToPshPrOffset")
        // TODO: implement — sets up the K-Mirror for target acquisition using the
        // pupil registration PIT to PSH rotational offset in the K-Mirror tracking demand calculation.
        // Per ICD 16.3, will eventually send FOC.KMirror.updatePitToPshOffset with this value.
        delay(1.seconds)
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "setupFocForAcquisition-complete",
            helpKey   = "help.setupFocForAcquisition",
            messageId = "msg.setupFocForAcquisition.complete"
        ))
    }

    // ICD 16.2.1.3 homeMechanisms — no parameters
    // Completion Type: longRunning
    onSetup("homeMechanisms") { command ->
        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "homeMechanisms-start",
            helpKey   = "help.homeMechanisms",
            messageId = "msg.homeMechanisms.start"
        ))
        println("IcsCommon: homeMechanisms — homing all APS Instrument mechanisms")
        // TODO: implement — homes all APS Instrument mechanisms.
        // Per ICD 16.3, likely involves the mechanism-owning assemblies, e.g.:
        // FOC.KMirror.setMode, APT.FilterWheel.selectFilter, APT.Detector.configDetector,
        // STIM.InsertionStage.selectSource. Exact set/order TBD once mechanism list is confirmed.
        delay(1.seconds)
        println("IcsCommon: homeMechanisms — homing all APS Instrument mechanisms complete")

        publishEvent(buildProcedureEvent(Prefix.apply(prefix),
            type      = ProcedureEventType.INFO_MESSAGE,
            dialogKey = "homeMechanisms-complete",
            helpKey   = "help.homeMechanisms",
            messageId = "msg.homeMechanisms.complete"
        ))
    }

}
