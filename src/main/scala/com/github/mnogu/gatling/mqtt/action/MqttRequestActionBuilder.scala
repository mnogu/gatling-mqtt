package com.github.mnogu.gatling.mqtt.action

import akka.actor.ActorDSL.actor
import akka.actor.ActorRef
import com.github.mnogu.gatling.mqtt.config.MqttProtocol
import com.github.mnogu.gatling.mqtt.request.builder.MqttAttributes
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.config.Protocols
import org.fusesource.mqtt.client.MQTT

class MqttRequestActionBuilder(mqttAttributes: MqttAttributes)
  extends ActionBuilder {

  override def registerDefaultProtocols(protocols: Protocols): Protocols =
    protocols + MqttProtocol.DefaultMqttProtocol

  override def build(next: ActorRef, protocols: Protocols): ActorRef = {
    val mqttProtocol = protocols.getProtocol[MqttProtocol].getOrElse(
      throw new UnsupportedOperationException(
        "MQTT Protocol wasn't registered"))
    val mqtt = new MQTT()

    actor(actorName("mqttRequest"))(new MqttRequestAction(
      mqtt, mqttAttributes, mqttProtocol, next))
  }
}
