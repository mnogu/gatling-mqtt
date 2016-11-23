package com.github.mnogu.gatling.mqtt

import com.github.mnogu.gatling.mqtt.protocol.MqttProtocolBuilder
import com.github.mnogu.gatling.mqtt.request.builder.MqttRequestBuilder
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session.Expression

object Predef {
  def mqtt(implicit configuration: GatlingConfiguration) = MqttProtocolBuilder(configuration)

  def mqtt(requestName: Expression[String]) =
    new MqttRequestBuilder(requestName)

}
