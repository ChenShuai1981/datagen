package com.datacloud.datagen.metricevent

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

case class MetricEvent(name: String, timestamp: Long, fields: Map[String, Any], tags: Map[String, String])

object MetricEventProducer extends App {
  val topicName = "metric-event"
  val bootstrapServers = "localhost:9092"

  val interval = 600L
  val loop = 1

  val producer = new MetricEventProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class MetricEventProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[MetricEvent](topicName, bootstrapServers, interval, loop) {

  override def genData = {
    for {
      name <- Gen.oneOf("Online", "Offline")
      timestamp <- Gen.const(System.currentTimeMillis())
      fields <- Gen.const(Map("load5" -> 26, "cpu" -> "AMD"))
      tags <- Gen.const(Map("cluster_name" -> "terminus-x"))
    } yield {
      MetricEvent(name, timestamp, fields, tags)
    }
  }
}
