package com.datacloud.datagen.dsp

import com.datacloud.datagen.AvroDataProducer
import com.datacloud.dsp.avro.bbtree.BBTreeImpBeaconObj
import org.scalacheck.Gen

object BBTreeImpBeaconObjProducer extends App {
  val topicName = "loc_BBTREE_IMPRESSION_BEACON"
  val bootstrapServers = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"

  val producer = new BBTreeImpBeaconObjProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 10)
  producer.run()
}

class BBTreeImpBeaconObjProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[BBTreeImpBeaconObj](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[BBTreeImpBeaconObj] = genBBTreeImpBeaconObj

  override def getKey(t: BBTreeImpBeaconObj): String = s"${t.getBidid}_${t.getPlacementid}_${t.getTs}"
}
