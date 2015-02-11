package com.github.mnogu.gatling.mqtt.config

import io.gatling.core.config.Protocol
import io.gatling.core.session.Expression

import org.fusesource.mqtt.client.QoS

object MqttProtocol {
  val DefaultMqttProtocol = MqttProtocol(
    host = None,
    optionPart = MqttProtocolOptionPart(
      clientId = None,
      cleanSession = None,
      keepAlive = None,
      userName = None,
      password = None,
      willTopic = None,
      willMessage = None,
      willQos = None,
      willRetain = None,
      version = None),
    reconnectPart = MqttProtocolReconnectPart(
      connectAttemptsMax = None,
      reconnectAttemptsMax = None,
      reconnectDelay = None,
      reconnectDelayMax = None,
      reconnectBackOffMultiplier = None),
    socketPart = MqttProtocolSocketPart(
      receiveBufferSize = None,
      sendBufferSize = None,
      trafficClass = None),
    throttlingPart = MqttProtocolThrottlingPart(
      maxReadRate = None,
      maxWriteRate = None))
}

case class MqttProtocol(
  host: Option[Expression[String]],
  optionPart: MqttProtocolOptionPart,
  reconnectPart: MqttProtocolReconnectPart,
  socketPart: MqttProtocolSocketPart,
  throttlingPart: MqttProtocolThrottlingPart) extends Protocol {

  def host(host: Expression[String]): MqttProtocol = copy(host = Some(host))

  // optionPart
  def clientId(clientId: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(clientId = Some(clientId)))
  def cleanSession(cleanSession: Boolean): MqttProtocol = copy(
    optionPart = optionPart.copy(cleanSession = Some(cleanSession)))
  def keepAlive(keepAlive: Short): MqttProtocol = copy(
    optionPart = optionPart.copy(keepAlive = Some(keepAlive)))
  def userName(userName: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(userName = Some(userName)))
  def password(password: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(password = Some(password)))
  def willTopic(willTopic: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(willTopic = Some(willTopic)))
  def willMessage(willMessage: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(willMessage = Some(willMessage)))
  def willQos(willQos: QoS): MqttProtocol = copy(
    optionPart = optionPart.copy(willQos = Some(willQos)))
  def willRetain(willRetain: Boolean): MqttProtocol = copy(
    optionPart = optionPart.copy(willRetain = Some(willRetain)))
  def version(version: Expression[String]): MqttProtocol = copy(
    optionPart = optionPart.copy(version = Some(version)))

  // reconnectPart
  def connectAttemptsMax(connectAttemptsMax: Long): MqttProtocol = copy(
    reconnectPart = reconnectPart.copy(
      connectAttemptsMax = Some(connectAttemptsMax)))
  def reconnectAttemptsMax(reconnectAttemptsMax: Long): MqttProtocol = copy(
    reconnectPart = reconnectPart.copy(
      reconnectAttemptsMax = Some(reconnectAttemptsMax)))
  def reconnectDelay(reconnectDelay: Long): MqttProtocol = copy(
    reconnectPart = reconnectPart.copy(
      reconnectDelay = Some(reconnectDelay)))
  def reconnectDelayMax(reconnectDelayMax: Long): MqttProtocol = copy(
    reconnectPart = reconnectPart.copy(
      reconnectDelayMax = Some(reconnectDelayMax)))
  def reconnectBackOffMultiplier(reconnectBackOffMultiplier: Double): MqttProtocol = copy(
    reconnectPart = reconnectPart.copy(
      reconnectBackOffMultiplier = Some(reconnectBackOffMultiplier)))

  // socketPart
  def receiveBufferSize(receiveBufferSize: Int): MqttProtocol = copy(
    socketPart = socketPart.copy(receiveBufferSize = Some(receiveBufferSize)))
  def sendBufferSize(sendBufferSize: Int): MqttProtocol = copy(
    socketPart = socketPart.copy(sendBufferSize = Some(sendBufferSize)))
  def trafficClass(trafficClass: Int): MqttProtocol = copy(
    socketPart = socketPart.copy(trafficClass = Some(trafficClass)))

  // throttlingPart
  def maxReadRate(maxReadRate: Int): MqttProtocol = copy(
    throttlingPart = throttlingPart.copy(maxReadRate = Some(maxReadRate)))
  def maxWriteRate(maxWriteRate: Int): MqttProtocol = copy(
    throttlingPart = throttlingPart.copy(maxWriteRate = Some(maxWriteRate)))
}

case class MqttProtocolOptionPart(
  clientId: Option[Expression[String]],
  cleanSession: Option[Boolean],
  keepAlive: Option[Short],
  userName: Option[Expression[String]],
  password: Option[Expression[String]],
  willTopic: Option[Expression[String]],
  willMessage: Option[Expression[String]],
  willQos: Option[QoS],
  willRetain: Option[Boolean],
  version: Option[Expression[String]])

case class MqttProtocolReconnectPart(
  connectAttemptsMax: Option[Long],
  reconnectAttemptsMax: Option[Long],
  reconnectDelay: Option[Long],
  reconnectDelayMax: Option[Long],
  reconnectBackOffMultiplier: Option[Double])

case class MqttProtocolSocketPart(
  receiveBufferSize: Option[Int],
  sendBufferSize: Option[Int],
  trafficClass: Option[Int])

case class MqttProtocolThrottlingPart(
  maxReadRate: Option[Int],
  maxWriteRate: Option[Int])