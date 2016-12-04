# Gatling-MQTT

An unofficial [Gatling](http://gatling.io/) stress test plugin
for [MQTT](http://mqtt.org/).

## Usage

### Cloning this repository

    $ git clone https://github.com/mnogu/gatling-mqtt.git
    $ cd gatling-mqtt

### Creating a jar file

Install [sbt](http://www.scala-sbt.org/) 0.13 if you don't have.
And create a jar file:

    $ sbt assembly

If you want to change the version of Gatling used to create a jar file,
change the following line in [`build.sbt`](build.sbt):

```scala
"io.gatling" % "gatling-core" % "2.2.3" % "provided",
```

and run `sbt assembly`.

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.11/gatling-mqtt-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.2.*/lib

###  Creating a simulation file

    $ cp gatling-mqtt/src/test/scala/com/github/mnogu/gatling/mqtt/test/MqttSimulation.scala /path/to/gatling-charts-highcharts-bundle-2.2.*/user-files/simulations
    $ cd /path/to/gatling-charts-highcharts-bundle-2.2.*
    $ vi user-files/simulations/MqttSimulation.scala

This plugin supports the following options:

* host
* clientId
* cleanSession
* keepAlive
* userName
* password
* willTopic
* willMessage
* willQos
* willRetain
* version
* connectAttemptsMax
* reconnectAttemptsMax
* reconnectDelay
* reconnectDelayMax
* reconnectBackOffMultiplier
* receiveBufferSize
* sendBufferSize
* trafficClass
* maxReadRate
* maxWriteRate

See the document of [mqtt-client](https://github.com/fusesource/mqtt-client)
for the description of these options.
For example, the `host` option corresponds `setHost()` method in mqtt-client.
That is, you can obtain an option name in this plugin
by removing `set` from a method name in mqtt-client
and then making the first character lowercase.

The following options also support [Expression](http://gatling.io/docs/2.2.3/session/expression_el.html):

* host
* clientId
* userName
* password
* willTopic
* willMessage
* version

Here is a sample simulation file:

```scala
import io.gatling.core.Predef._
import org.fusesource.mqtt.client.QoS
import scala.concurrent.duration._

import com.github.mnogu.gatling.mqtt.Predef._

class MqttSimulation extends Simulation {
  val mqttConf = mqtt
    // MQTT broker
    .host("tcp://localhost:1883")

  val scn = scenario("MQTT Test")
    .exec(mqtt("request")
      // topic: "foo"
      // payload: "Hello"
      // QoS: AT_LEAST_ONCE
      // retain: false
      .publish("foo", "Hello", QoS.AT_LEAST_ONCE, retain = false))

  setUp(
    scn
      .inject(constantUsersPerSec(10) during(90 seconds)))
    .protocols(mqttConf)
}
```

The following parameters of `publish()` support Expression:

* topic
* payload

Here is a bit complex sample simulation file:

```scala
import io.gatling.core.Predef._
import org.fusesource.mqtt.client.QoS
import scala.concurrent.duration._

import com.github.mnogu.gatling.mqtt.Predef._

class MqttSimulation extends Simulation {
  val mqttConf = mqtt
    .host("tcp://localhost:1883")
    // clientId: the values of "client" column in mqtt.csv
    //
    // See below for mqtt.csv.
    .clientId("${client}")

  val scn = scenario("MQTT Test")
    // The content of mqtt.csv would be like this:
    //
    //   client,topic,payload
    //   clientId1,topic1,payload1
    //   clientId2,topic2,payload2
    //   ...
    .feed(csv("mqtt.csv").circular)
    .exec(mqtt("request")
      // topic: the values of "topic" column in mqtt.csv
      // payload: the values of "payload" column in mqtt.csv
      // QoS: AT_LEAST_ONCE
      // retain: false
      .publish("${topic}", "${payload}", QoS.AT_LEAST_ONCE, retain = false))

  setUp(
    scn
      .inject(constantUsersPerSec(10) during(90 seconds)))
    .protocols(mqttConf)
}
```

### Running a stress test

After starting an MQTT broker, run a stress test:

    $ bin/gatling.sh

## License

Apache License, Version 2.0
