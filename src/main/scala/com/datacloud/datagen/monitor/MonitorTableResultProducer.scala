package com.datacloud.datagen.monitor

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object MonitorTableResultProducer extends App with KafkaEnv {
  val topicName = envPrefix + "MONITOR_11123_TS_TABLE"
  val producer = new MonitorTableResultProducer(topicName, bootstrapServers, 600, 1)
  producer.run()
}

class MonitorTableResultProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[MonitorTableResult](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    tenantId <- Gen.const(29L)
    hitTime <- Gen.const(System.currentTimeMillis())
    hitCounts <- Gen.choose(1, 100)
  } yield {
    MonitorTableResult(tenantId, hitTime, hitCounts)
  }

  override def getKey(t: MonitorTableResult): String = t.hitTime.toString
}