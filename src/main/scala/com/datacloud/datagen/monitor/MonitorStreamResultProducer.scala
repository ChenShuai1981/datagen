package com.datacloud.datagen.monitor

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object MonitorStreamResultProducer extends App {
  val topicName = "sit_MONITOR_RULESET_29_4321_STREAM"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
  //  val bootstrapServers = "localhost:9092"
  val interval = 60L
  val loop = 1

  val producer = new MonitorStreamResultProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class MonitorStreamResultProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[MonitorStreamResult](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    tenantId <- Gen.const(29L)
    productCode <- Gen.const("test")
    eventCode <- Gen.const("loanApply")
    terminal <- Gen.const("GENERAL")
    occurTime <- Gen.const(System.currentTimeMillis())
  } yield {
    MonitorStreamResult(tenantId, productCode, eventCode, terminal, occurTime)
  }

}