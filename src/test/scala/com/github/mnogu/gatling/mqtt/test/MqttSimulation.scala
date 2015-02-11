package com.github.mnogu.gatling.mqtt.test

import io.gatling.core.Predef._
import org.fusesource.mqtt.client.QoS
import scala.concurrent.duration._

import com.github.mnogu.gatling.mqtt.Predef._

class MqttSimulation extends Simulation {
  val mqttConf = mqtt.host("tcp://localhost:1883")

  val scn = scenario("MQTT Test")
    .exec(mqtt("request")
    .publish("foo", "Hello", QoS.AT_LEAST_ONCE, retain = false))

  setUp(
    scn
      .inject(constantUsersPerSec(10) during(90 seconds)))
    .protocols(mqttConf)
}
