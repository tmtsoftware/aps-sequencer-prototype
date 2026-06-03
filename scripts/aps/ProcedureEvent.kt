package aps

import csw.params.events.SystemEvent
import csw.params.events.EventName
import csw.prefix.models.Prefix
import csw.prefix.javadsl.JSubsystem
import csw.params.javadsl.JKeyType
import csw.params.javadsl.JUnits

// ---------------------------------------------------------------------------
// apsProcedureEvent — shared definitions
// ---------------------------------------------------------------------------

object ProcedureEventType {
    const val INFO_MESSAGE  = "INFO_MESSAGE"
    const val WARN_MESSAGE  = "WARN_MESSAGE"
    const val USER_PROMPT   = "USER_PROMPT"
    const val VIZ_DISPLAY   = "VIZ_DISPLAY"
}

private val eventPrefix  = Prefix.apply(JSubsystem.APS, "sequencer")
private val eventName    = EventName("apsProcedureEvent")

private val typeKey      = JKeyType.StringKey().make("type",      JUnits.NoUnits)
private val dialogKeyKey = JKeyType.StringKey().make("dialogKey", JUnits.NoUnits)
private val helpKeyKey   = JKeyType.StringKey().make("helpKey",   JUnits.NoUnits)
private val messageIdKey = JKeyType.StringKey().make("messageId", JUnits.NoUnits)

fun buildProcedureEvent(
    type: String,
    dialogKey: String,
    helpKey: String,
    messageId: String
): SystemEvent =
    SystemEvent(eventPrefix, eventName)
        .add(typeKey.set(type))
        .add(dialogKeyKey.set(dialogKey))
        .add(helpKeyKey.set(helpKey))
        .add(messageIdKey.set(messageId))
