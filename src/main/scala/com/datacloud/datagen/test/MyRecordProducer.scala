package com.datacloud.datagen.test

import com.datacloud.datagen.AvroDataProducer
import com.example.MyRecord
import org.scalacheck.Gen


object MyRecordProducer extends App {
  val topicName = "my-record-topic2"

  val bootstrapServers = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"


  val producer = new MyRecordProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
  producer.run()
}

class MyRecordProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[MyRecord](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[MyRecord] = {
    for {
      id <- Gen.uuid.map(_.toString)
      birthday <- Gen.choose(10000, 20000)
      ts <- Gen.choose(1000000, 2000000)
    } yield {
      val myrecord = new MyRecord()
      myrecord.setId(id)
      myrecord.setBirthday(birthday)
      myrecord.setTs(ts)
      myrecord
    }
  }

  override def getKey(t: MyRecord): String = s"${t.getId}"
}