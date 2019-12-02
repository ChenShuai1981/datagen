package com.datacloud.datagen.monitor

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object MonitorStreamResultProducer extends App with KafkaEnv {
  val topicName = envPrefix + "MONITOR_RULESET_29_4321_STREAM"
  val producer = new MonitorStreamResultProducer(topicName, bootstrapServers, 600, 1)
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

  override def getKey(t: MonitorStreamResult): String = t.occurTime.toString
}