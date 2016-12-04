package com.github.mnogu.gatling.mqtt.action

import com.github.mnogu.gatling.mqtt.protocol.{MqttComponents, MqttProtocol}
import com.github.mnogu.gatling.mqtt.request.builder.MqttAttributes
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext

class MqttRequestActionBuilder(mqttAttributes: MqttAttributes)
  extends ActionBuilder {

  override def build(
    ctx: ScenarioContext, next: Action
  ): Action = {
    import ctx._

    val mqttComponents : MqttComponents = protocolComponentsRegistry.components(MqttProtocol.MqttProtocolKey)
    
    new MqttRequestAction(
      mqttAttributes,
      coreComponents,
      mqttComponents.mqttProtocol,
      next
    )
  }
}
