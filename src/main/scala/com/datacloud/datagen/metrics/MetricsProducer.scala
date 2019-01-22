package com.datacloud.datagen.metrics

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

case class Metrics(key: String, value: String)

object MetricsProducer extends App with KafkaEnv {
  val topicName = envPrefix + "METRICS_STM"
  val producer = new MetricsProducer(topicName, bootstrapServers, 600, 1)
  producer.run()
}

class MetricsProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[Metrics](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    key <- Gen.const("bankno_987")
    value <- Gen.const("20")
  } yield {
    val metrics = Metrics(key, value)
    metrics
  }

  override def getKey(t: Metrics): String = t.key
}