package com.github.mnogu.gatling.mqtt.protocol

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

/**
  *
  */
case class MqttComponents(mqttProtocol: MqttProtocol) extends ProtocolComponents {
    override def onStart: Option[(Session) => Session] = None

    override def onExit: Option[(Session) => Unit] = None
}
