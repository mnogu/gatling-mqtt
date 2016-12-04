package com.github.mnogu.gatling.mqtt.action

import com.github.mnogu.gatling.mqtt.protocol.MqttProtocol
import com.github.mnogu.gatling.mqtt.request.builder.MqttAttributes
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.ClockSingleton._
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.Predef._
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session._
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import org.fusesource.mqtt.client.{Callback, CallbackConnection, MQTT, QoS}

class MqttRequestAction(
  val mqttAttributes: MqttAttributes,
  val coreComponents : CoreComponents,
  val mqttProtocol: MqttProtocol,
  val next: Action)
   extends ExitableAction with NameGen {

  val statsEngine = coreComponents.statsEngine

  override val name = genName("mqttRequest")

  private def configureHost(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.host match {
      case Some(host) => host(session).map { resolvedHost =>
        mqtt.setHost(resolvedHost)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureClientId(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.clientId match {
      case Some(clientId) => clientId(session).map { resolvedClientId =>
        mqtt.setClientId(resolvedClientId)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureUserName(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.userName match {
      case Some(userName) => userName(session).map { resolvedUserName =>
        mqtt.setUserName(resolvedUserName)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configurePassword(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.password match {
      case Some(password) => password(session).map { resolvedPassword =>
        mqtt.setPassword(resolvedPassword)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureWillTopic(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.willTopic match {
      case Some(willTopic) => willTopic(session).map { resolvedWillTopic =>
        mqtt.setWillTopic(resolvedWillTopic)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureWillMessage(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.willMessage match {
      case Some(willMessage) => willMessage(session).map { resolvedWillMessage =>
        mqtt.setWillMessage(resolvedWillMessage)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureVersion(session: Session)(mqtt: MQTT): Validation[MQTT] = {
    mqttProtocol.optionPart.version match {
      case Some(version) => version(session).map { resolvedVersion =>
        mqtt.setVersion(resolvedVersion)
        mqtt
      }
      case None => mqtt
    }
  }

  private def configureOptions(mqtt: MQTT) = {
    // optionPart
    val cleanSession = mqttProtocol.optionPart.cleanSession
    if (cleanSession.isDefined) {
      mqtt.setCleanSession(cleanSession.get)
    }
    val keepAlive = mqttProtocol.optionPart.keepAlive
    if (keepAlive.isDefined) {
      mqtt.setKeepAlive(keepAlive.get)
    }
    val willQos = mqttProtocol.optionPart.willQos
    if (willQos.isDefined) {
      mqtt.setWillQos(willQos.get)
    }
    val willRetain = mqttProtocol.optionPart.willRetain
    if (willRetain.isDefined) {
      mqtt.setWillRetain(willRetain.get)
    }

    // reconnectPart
    val connectAttemptsMax = mqttProtocol.reconnectPart.connectAttemptsMax
    if (connectAttemptsMax.isDefined) {
      mqtt.setConnectAttemptsMax(connectAttemptsMax.get)
    }
    val reconnectAttemptsMax = mqttProtocol.reconnectPart.reconnectAttemptsMax
    if (reconnectAttemptsMax.isDefined) {
      mqtt.setReconnectAttemptsMax(reconnectAttemptsMax.get)
    }
    val reconnectDelay = mqttProtocol.reconnectPart.reconnectDelay
    if (reconnectDelay.isDefined) {
      mqtt.setReconnectDelay(reconnectDelay.get)
    }
    val reconnectDelayMax = mqttProtocol.reconnectPart.reconnectDelayMax
    if (reconnectDelayMax.isDefined) {
      mqtt.setReconnectDelayMax(reconnectDelayMax.get)
    }
    val reconnectBackOffMultiplier =
      mqttProtocol.reconnectPart.reconnectBackOffMultiplier
    if (reconnectBackOffMultiplier.isDefined) {
      mqtt.setReconnectBackOffMultiplier(reconnectBackOffMultiplier.get)
    }

    // socketPart
    val receiveBufferSize = mqttProtocol.socketPart.receiveBufferSize
    if (receiveBufferSize.isDefined) {
      mqtt.setReceiveBufferSize(receiveBufferSize.get)
    }
    val sendBufferSize = mqttProtocol.socketPart.sendBufferSize
    if (sendBufferSize.isDefined) {
      mqtt.setSendBufferSize(sendBufferSize.get)
    }
    val trafficClass = mqttProtocol.socketPart.trafficClass
    if (trafficClass.isDefined) {
      mqtt.setTrafficClass(trafficClass.get)
    }

    // throttlingPart
    val maxReadRate = mqttProtocol.throttlingPart.maxReadRate
    if (maxReadRate.isDefined) {
      mqtt.setMaxReadRate(maxReadRate.get)
    }
    val maxWriteRate = mqttProtocol.throttlingPart.maxWriteRate
    if (maxWriteRate.isDefined) {
      mqtt.setMaxWriteRate(maxWriteRate.get)
    }
  }

  override def execute(session: Session): Unit = recover(session) {
    val mqtt = new MQTT()

    configureHost(session)(mqtt)
      .flatMap(configureClientId(session))
      .flatMap(configureUserName(session))
      .flatMap(configurePassword(session))
      .flatMap(configureWillTopic(session))
      .flatMap(configureWillMessage(session))
      .flatMap(configureVersion(session)).map { resolvedMqtt =>

      configureOptions(resolvedMqtt)

      val connection = resolvedMqtt.callbackConnection()
      connection.connect(new Callback[Void] {
        override def onSuccess(void: Void): Unit = {
          mqttAttributes.requestName(session).flatMap { resolvedRequestName =>
            mqttAttributes.topic(session).flatMap { resolvedTopic =>
              sendRequest(
                resolvedRequestName,
                connection,
                resolvedTopic,
                mqttAttributes.payload,
                mqttAttributes.qos,
                mqttAttributes.retain,
                session)
            }
          }
        }
        override def onFailure(value: Throwable): Unit = {
          mqttAttributes.requestName(session).map { resolvedRequestName =>
              statsEngine.reportUnbuildableRequest(session, resolvedRequestName, value.getMessage)
          }
          connection.disconnect(null)
        }
      })
    }
  }

  private def sendRequest(
      requestName: String,
      connection: CallbackConnection,
      topic: String,
      payload: Expression[String],
      qos: QoS,
      retain: Boolean,
      session: Session): Validation[Unit] = {

    payload(session).map { resolvedPayload =>
      val requestStartDate = nowMillis

      connection.publish(
        topic, resolvedPayload.getBytes, qos, retain, new Callback[Void] {
          override def onFailure(value: Throwable): Unit =
            writeData(isSuccess = false, Some(value.getMessage))

          override def onSuccess(void: Void): Unit =
            writeData(isSuccess = true, None)

          private def writeData(isSuccess: Boolean, message: Option[String]) = {
            val requestEndDate = nowMillis

            statsEngine.logResponse(
              session,
              requestName,
              ResponseTimings(startTimestamp = requestStartDate, endTimestamp = requestEndDate),
              if (isSuccess) OK else KO,
              None,
              message
            )

            next ! session

            connection.disconnect(null)
          }
        })
    }
  }
}
