package com.datacloud.datagen

import java.util.Properties

import com.datacloud.datagen.util.JsonUtil
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks

abstract class JsonDataProducer[T](topicName: String,
                                   bootstrapServers: String = "localhost:9092",
                                   interval: Long = 60, loop: Int = 10) extends DataProducer with GeneratorDrivenPropertyChecks {

  def genData: Gen[T]

  def getKey(t: T): String

  def run() = {
    val props = new Properties
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put("enable.idempotence", "true")

    val producer: Producer[String, String] = new KafkaProducer[String, String](props)
    for (i <- 1 to loop) {
      forAll(genData) {
        (data: T) => {
//          val jsonString =
//            """
//              |{
//              |	"input_clientData_deviceInfo_generalDeviceId": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_country": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_province": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_city": "",
//              |	"input_indivDeviceGeoLatitude__ROLE__APPLICANT": 0.0,
//              |	"input_indivDeviceGeoLongitude__ROLE__APPLICANT": 0.0,
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_country": "",
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_province": "",
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_city": ""
//              |}
//            """.stripMargin
//          producer.send(new ProducerRecord[String, String](topicName, null, null, jsonString))
          val jsonString = JsonUtil.toJson(data)
          producer.send(new ProducerRecord[String, String](topicName, null, getKey(data), jsonString))
          println(jsonString)
          Thread.sleep(interval)
        }
      }
    }

    producer.flush()
    producer.close()
  }
}
