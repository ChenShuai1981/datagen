package com.datacloud.datagen.metrics

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

case class Metrics(INDIVBANKNO: String, LINKNIKCOUNT: Int)

object MetricsProducer extends App {
  val topicName = "preprod_RISK_INVOCATION_HISTORY_FLATTEN"
  val bootstrapServers = "10.12.0.175:9092"
  val interval = 60L
  val loop = 1

  val producer = new MetricsProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class MetricsProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[Metrics](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    key <- Gen.const("bankno_987")
    value <- Gen.const(20)
  } yield {
    val metrics = Metrics(key, value)
    metrics
  }

  override def getKey(t: Metrics): String = t.INDIVBANKNO.toString
}