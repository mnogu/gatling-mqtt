package com.github.mnogu.gatling.mqtt.request.builder

import com.github.mnogu.gatling.mqtt.action.MqttRequestActionBuilder
import com.github.mnogu.gatling.mqtt.config.MqttProtocol
import io.gatling.core.session.Expression
import org.fusesource.mqtt.client.QoS

case class MqttAttributes(
  requestName: Expression[String],
  topic: Expression[String],
  payload: Expression[String],
  qos: QoS,
  retain: Boolean)

case class MqttRequestBuilder(requestName: Expression[String]) {
  def publish(
    topic: Expression[String],
    payload: Expression[String],
    qos: QoS,
    retain: Boolean): MqttRequestActionBuilder =
    new MqttRequestActionBuilder(MqttAttributes(
      requestName,
      topic,
      payload,
      qos,
      retain))
}
