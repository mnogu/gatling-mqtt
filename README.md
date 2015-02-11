# Gatling-MQTT

An unofficial [Gatling](http://gatling.io/) stress test library
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
"io.gatling" % "gatling-core" % "2.1.4" % "provided",
```

and run `sbt assembly`.

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.11/gatling-mqtt-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.1.*/lib

###  Creating a simulation file

    $ cd /path/to/gatling-charts-highcharts-bundle-2.1.*
    $ vi user-files/simulations/MqttSimulation.scala

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

### Running a stress test

After starting an MQTT broker, run a stress test:

    $ bin/gatling.sh

## License

Apache License, Version 2.0
