package com.datacloud.datagen.sensor

import com.datacloud.datagen.AvroDataProducer
import com.example.Sensor
import org.scalacheck.Gen


object SensorProducer extends App {
  val topicName = "sensor-topic"

  val bootstrapServers = "localhost:9092"
  val schemaRegistryUrl = "http://localhost:8081"


  val producer = new SensorProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
  producer.run()
}

class SensorProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[Sensor](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[Sensor] = {
    for {
      id <- Gen.uuid.map(_.toString)
      acceleration <- Gen.choose(1.0f, 10.0f)
      externalTemperature <- Gen.choose(30.0f, 60.0f)
      internalTemperature <- Gen.choose(40.0f, 70.0f)
      velocity <- Gen.choose(40.0f, 70.0f)
    } yield {
      val sensor = new Sensor()
      sensor.setAcceleration(acceleration)
      sensor.setExternalTemperature(externalTemperature)
      sensor.setInternalTemperature(internalTemperature)
      sensor.setVelocity(velocity)
      sensor.setId(id)

      sensor
    }
  }

  override def getKey(t: Sensor): String = s"${t.getId}"
}
