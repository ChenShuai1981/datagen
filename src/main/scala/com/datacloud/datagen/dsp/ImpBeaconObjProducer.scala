package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.ImpBeaconObj
import org.scalacheck.Gen

object ImpBeaconObjProducer extends App {
  val topicName = "IMP_BEACON"

//  val bootstrapServers = "localhost:9092"
//  val schemaRegistryUrl = "http://localhost:8081"

  val bootstrapServers = "10.12.0.129:9092"
  val schemaRegistryUrl = "http://10.12.0.129:8087"

  val producer = new ImpBeaconObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 1000L, 10)
  producer.run()
}

class ImpBeaconObjProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[ImpBeaconObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[ImpBeaconObj] = genImpBeaconObj

  override def getKey(t: ImpBeaconObj): String = s"${t.getBidid}_${t.getTagid}_${t.getTs}"
}
