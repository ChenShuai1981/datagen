package com.datacloud.datagen.monitor

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object MonitorTableResultProducer extends App {
  val topicName = "sit_MONITOR_11123_TS_TABLE"
  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
  //  val bootstrapServers = "localhost:9092"
  val interval = 60L
  val loop = 1

  val producer = new MonitorTableResultProducer(topicName, bootstrapServers, interval, loop)
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

}