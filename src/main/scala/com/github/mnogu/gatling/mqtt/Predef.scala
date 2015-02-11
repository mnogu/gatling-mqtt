package com.github.mnogu.gatling.mqtt

import com.github.mnogu.gatling.mqtt.config.MqttProtocol
import com.github.mnogu.gatling.mqtt.request.builder.{MqttAttributes, MqttRequestBuilder}
import io.gatling.core.session.Expression

object Predef {
  def mqtt = MqttProtocol.DefaultMqttProtocol

  def mqtt(requestName: Expression[String]) =
    new MqttRequestBuilder(requestName)

}
