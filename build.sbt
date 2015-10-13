organization := "com.github.mnogu"

name := "gatling-mqtt"

version := "0.0.4-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % "2.1.7" % "provided",
  "org.fusesource.mqtt-client" % "mqtt-client" % "1.11"
)

// Gatling contains scala-library
assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(includeScala = false)
