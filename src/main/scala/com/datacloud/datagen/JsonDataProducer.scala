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
//              |	"eventId": 0,
//              |	"creditScore": 0.0,
//              |	"fraudScore": 0.0,
//              |	"decision": "PASS",
//              |	"decisionLevel": 0,
//              |	"productId": 0,
//              |	"productCode": "",
//              |	"antifraudDetail_riskLevel": "",
//              |	"antifraudDetail_antifraudDecision": "",
//              |	"creditDetail_creditDecision": "",
//              |	"creditDetail_compoundPeriod": 0,
//              |	"creditDetail_amount": 0.0,
//              |	"creditDetail_rateValue": 0.0,
//              |	"creditDetail_rateType": "",
//              |	"creditDetail_creditStartDate": 0,
//              |	"creditDetail_creditEndDate": 0,
//              |	"strategySetName": "",
//              |	"terminal": "",
//              |	"creditStrategyRCId": 0,
//              |	"userId": 0,
//              |	"riskProcessId": 0,
//              |	"eventCode": "",
//              |	"executionId": 0,
//              |	"strategySetId": 0,
//              |	"tenantId": 0,
//              |	"eventName": "",
//              |	"input_indivBankNo__ROLE__APPLICANT": "",
//              |	"input_indivID__ROLE__APPLICANT": "",
//              |	"input_indivID__ROLE__APPLICANT_province": "",
//              |	"input_indivGender__ROLE__APPLICANT": "",
//              |	"input_indivPhone__ROLE__APPLICANT": "",
//              |	"input_indivName__ROLE__APPLICANT": "",
//              |	"input_indivAge__ROLE__APPLICANT": "",
//              |	"input_indivDeviceId__ROLE__APPLICANT": "",
//              |	"input_clientData_deviceInfo_generalDeviceId": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_country": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_province": "",
//              |	"input_indivIpAddress__ROLE__APPLICANT_location_city": "",
//              |	"input_indivDeviceGeoLatitude__ROLE__APPLICANT": 0.0,
//              |	"input_indivDeviceGeoLongitude__ROLE__APPLICANT": 0.0,
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_country": "",
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_province": "",
//              |	"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_city": "",
//              |	"hitRuleSet_decision": "",
//              |	"hitRuleSet_ruleSetId": 0,
//              |	"hitRuleSet_ruleSetName": "",
//              |	"hitRuleSet_ruleSetMode": "",
//              |	"hitRule_ruleName": "",
//              |	"hitRule_ruleId": 0,
//              |	"hitRule_isHit": true,
//              |	"input_indivDeviceModelNo__ROLE__APPLICANT": "",
//              |	"input_clientIndivId__ROLE__APPLICANT": ""
//              |}
//            """.stripMargin
          val jsonString = """{"occurTime":0,"strategySetName":"strategySetName","creditStrategyRCId":0,"riskProcessId":0,"strategySetId":0,"eventName":"eventName","eventId":0,"creditScore":0.0,"fraudScore":0.0,"decision":"reject","productId":0,"antifraudDetail_riskLevel":"high","antifraudDetail_antifraudDecision":"reject","terminal":"terminal","userId":0,"admissionDetail_admissionDecision":"reject","eventCode":"eventCode","executionId":0,"input_clientData_deviceInfo_generalDeviceId":"deviceId","input_indivID__ROLE__APPLICANT":"indivID","input_indivName__ROLE__APPLICANT":"indivName","input_indivID__ROLE__APPLICANT_province":"","productCode":"productCode","tenantId":999999999,"hitRuleSet_decision":"reject","hitRuleSet_ruleSetName":"ruleSetName","hitRuleSet_ruleSetMode":"firstMode","hitRuleSet_ruleSetId":0,"hitRule_ruleName":"myRuleName","hitRule_ruleId":0,"hitRule_isHit":true,"decisionLevel":0,"input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_province":"","input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_country":"","input_indivDeviceGeo__ROLE__APPLICANT_result_addressComponent_city":""，"input_indivDeviceId__ROLE__APPLICANT":"","input_indivIpAddress__ROLE__APPLICANT":"","input_indivIpAddress__ROLE__APPLICANT_location_country":"","input_indivIpAddress__ROLE__APPLICANT_location_city":"","input_indivIpAddress__ROLE__APPLICANT_location_province":"","input_indivDeviceGeoLatitude__ROLE__APPLICANT":0.0,"input_indivDeviceGeoLongitude__ROLE__APPLICANT":0.0,"input_indivDeviceModelNo__ROLE__APPLICANT":"","input_clientIndivId__ROLE__APPLICANT":"","input_indivPhone__ROLE__APPLICANT":"","input_indivAge__ROLE__APPLICANT":0,"input_indivGender__ROLE__APPLICANT":"","input_indivBankNo__ROLE__APPLICANT":"","creditDetail_rateValue":0.0,"creditDetail_rateType":"","creditDetail_creditStartDate":0,"creditDetail_creditEndDate":0}"""
          producer.send(new ProducerRecord[String, String](topicName, null, null, jsonString))
//          val jsonString = JsonUtil.toJson(data)
//          producer.send(new ProducerRecord[String, String](topicName, null, getKey(data), jsonString))
          println(jsonString)
          Thread.sleep(interval)
        }
      }
    }

    producer.flush()
    producer.close()
  }
}
