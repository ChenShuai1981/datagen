package com.datacloud.datagen.metrics

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

case class Metrics(key: String, value: String)

object MetricsProducer extends App {
  val topicName = "LOC_METRICS"
  val bootstrapServers = "localhost:9092"
  val interval = 60L
  val loop = 1

  val producer = new MetricsProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class MetricsProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[Metrics](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    key <- Gen.const("BANKNO|cardStory_linkNikCount|bankno_83")
    value <- Gen.const("2")
  } yield {
    val metrics = Metrics(key, value)
    metrics
  }

  override def getKey(t: Metrics): String = t.key.toString
}