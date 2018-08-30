package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.ClickBeaconObj
import org.scalacheck.Gen

object ClickBeaconObjProducer extends App {
  val topicName = "CLK_BEACON"
//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val bootstrapServers = "10.12.0.129:9092"
  val schemaRegistryUrl = "http://10.12.0.129:8087"

  val producer = new ClickBeaconObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 10)
  producer.run()
}

class ClickBeaconObjProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[ClickBeaconObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[ClickBeaconObj] = genClickBeaconObj

  override def getKey(t: ClickBeaconObj): String = s"${t.getBidid}_${t.getTagid}_${t.getTs}"
}
