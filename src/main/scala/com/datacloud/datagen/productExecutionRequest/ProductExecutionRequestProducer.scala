package com.datacloud.datagen.productExecutionRequest

import com.datacloud.datagen.history.{genEventCode, genProductCode, genTerminal}
import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object ProductExecutionRequestProducer extends App with KafkaEnv {
  val topicName = envPrefix + "PRODUCT_EXECUTION_REQUEST_DATA"
  val producer = new ProductExecutionRequestProducer(topicName, bootstrapServers, 1000, 1)
  producer.run()
}

case class ProductExecutionRequest(riskProcessId: Long,
                                   executionId: Long,
                                   tenantId: Long,
                                   productCode: String,
                                   terminal: String,
                                   eventCode: String,
                                   eventTime: Long,
                                   eventData: Map[String, Any])

class ProductExecutionRequestProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[ProductExecutionRequest](topicName, bootstrapServers, interval, loop) {

  override def genData = for {
    riskProcessId <- Gen.choose(2000000000L, 3000000000L)
    executionId <- Gen.uuid.map(uuid => Math.abs(uuid.getMostSignificantBits + uuid.getLeastSignificantBits))
    tenantId <- Gen.const(15L)
    productCode <- genProductCode
    terminal <- genTerminal
    eventCode <- genEventCode
    eventTime <- Gen.const(System.currentTimeMillis())
    eventData <- genEventData(eventCode)
  } yield {
    val productExecutionRequest = ProductExecutionRequest(riskProcessId, executionId, tenantId,
      productCode, terminal, eventCode, eventTime, eventData)

    productExecutionRequest.copy(tenantId = 15L).copy(productCode = "ABC")
    val mmap = scala.collection.mutable.Map(productExecutionRequest.eventData.toSeq: _*)
    mmap("indivPhone") = "1234567890"
    productExecutionRequest.copy(eventData = mmap.toMap)
  }

  override def getKey(t: ProductExecutionRequest): String = t.riskProcessId + "_" + t.executionId

  // 根据不同事件生成不同的eventData
  private def genEventData(eventCode: String): Gen[Map[String, Any]] = {
    val genEvent = eventCode match {
      case "registry" => genRegistryEvent
      case "login" => genLoginEvent
      case "apply" => genApplyEvent
      case "credit" => genCreditEvent
      case "withdraw" => genWithdrawEvent
      case _ => Gen.const(Map.empty[String, Any])
    }
    for {
      eventData <- genEvent
    } yield eventData
  }

  // 注册事件
  private def genRegistryEvent: Gen[Map[String, Any]] = {
    for {
      indivDeviceId <- genDeviceId
      indivDeviceIfProxy <- genDeviceIfProxy
      indivDeviceIfSimulator <- genDeviceIfSimulator
      indivPhone <- genPhone
      indivDeviceModelNo <- genDeviceModelNo
      indivIpAddress <- genIP
      indivDeviceMacAddress <- genIP
      (indivDeviceGeoLatitude, indivDeviceGeoLongitude) <- genGPS
    } yield {
      Map(
        "indivDeviceId" -> indivDeviceId,
        "indivDeviceIfProxy" -> indivDeviceIfProxy,
        "indivDeviceIfSimulator" -> indivDeviceIfSimulator,
        "indivPhone" -> indivPhone,
        "indivDeviceModelNo" -> indivDeviceModelNo,
        "indivIpAddress" -> indivIpAddress,
        "indivDeviceMacAddress" -> indivDeviceMacAddress,
        "indivDeviceGeoLatitude" -> indivDeviceGeoLatitude,
        "indivDeviceGeoLongitude" -> indivDeviceGeoLongitude
      )
    }
  }

  // 名单授信事件
  private def genCreditEvent: Gen[Map[String, Any]] = {
    for {
      indivDeviceId <- genDeviceId
      indivDeviceIfProxy <- genDeviceIfProxy
      indivDeviceIfSimulator <- genDeviceIfSimulator
      indivPhone <- genPhone
      indivID <- genCertNo
      indivName <- genName
      indivIpAddress <- genIP
      indivDeviceMacAddress <- genIP
      (indivDeviceGeoLatitude, indivDeviceGeoLongitude) <- genGPS
      indivEmergentContacts_0_phone <- genPhone // 紧急联系人手机号
      indivEmergentContacts_0_name <- genName // 紧急联系人姓名
      indivEmergentContacts_0_id <- genCertNo // 紧急联系人身份证号
    } yield {
      Map(
        "indivDeviceId" -> indivDeviceId,
        "indivDeviceIfProxy" -> indivDeviceIfProxy,
        "indivDeviceIfSimulator" -> indivDeviceIfSimulator,
        "indivPhone" -> indivPhone,
        "indivID" -> indivID,
        "indivName" -> indivName,
        "indivIpAddress" -> indivIpAddress,
        "indivDeviceMacAddress" -> indivDeviceMacAddress,
        "indivDeviceGeoLatitude" -> indivDeviceGeoLatitude,
        "indivDeviceGeoLongitude" -> indivDeviceGeoLongitude,
        "indivEmergentContacts_0_phone" -> indivEmergentContacts_0_phone,
        "indivEmergentContacts_0_name" -> indivEmergentContacts_0_name,
        "indivEmergentContacts_0_id" -> indivEmergentContacts_0_id
      )
    }
  }

  // 申请信息填写事件
  private def genApplyEvent: Gen[Map[String, Any]] = {
    for {
      indivApplyAddress <- Gen.identifier // 联系地址
      indivPhone <- genPhone
      indivID <- genCertNo
      indivName <- genName
      indivContactPhone <- genPhone // 联系人手机号
    } yield {
      Map(
        "indivApplyAddress" -> indivApplyAddress,
        "indivPhone" -> indivPhone,
        "indivID" -> indivID,
        "indivName" -> indivName,
        "indivContactPhone" -> indivContactPhone
      )
    }
  }

  // 提现事件
  private def genWithdrawEvent: Gen[Map[String, Any]] = {
    for {
      indivDeviceId <- genDeviceId
      indivDeviceIfProxy <- genDeviceIfProxy
      indivDeviceIfSimulator <- genDeviceIfSimulator
      indivPhone <- genPhone
      indivName <- genName
      indivID <- genCertNo
      indivIpAddress <- genIP
      indivDeviceMacAddress <- genIP
      (indivDeviceGeoLatitude, indivDeviceGeoLongitude) <- genGPS
      withdrawAmount <- Gen.choose(1000.00, 100000.00).map(_.toString)
    } yield {
      Map(
        "indivDeviceId" -> indivDeviceId,
        "indivDeviceIfProxy" -> indivDeviceIfProxy,
        "indivDeviceIfSimulator" -> indivDeviceIfSimulator,
        "indivPhone" -> indivPhone,
        "indivName" -> indivName,
        "indivID" -> indivID,
        "indivIpAddress" -> indivIpAddress,
        "indivDeviceMacAddress" -> indivDeviceMacAddress,
        "indivDeviceGeoLongitude" -> indivDeviceGeoLongitude,
        "indivDeviceGeoLatitude" -> indivDeviceGeoLatitude,
        "withdrawAmount" -> withdrawAmount
      )
    }
  }

  // 登录事件
  private def genLoginEvent: Gen[Map[String, Any]] = {
    for {
      indivDeviceId <- genDeviceId // optional
      indivDeviceIfProxy <- genDeviceIfProxy
      indivDeviceIfSimulator <- genDeviceIfSimulator
      indivPhone <- genPhone
      indivFaceIfVerified <- genFaceIfVerified
      indivIpAddress <- genIP
      indivDeviceMacAddress <- genIP
      (indivDeviceGeoLatitude, indivDeviceGeoLongitude) <- genGPS
    } yield {
      Map(
        "indivDeviceId" -> indivDeviceId,
        "indivDeviceIfProxy" -> indivDeviceIfProxy,
        "indivDeviceIfSimulator" -> indivDeviceIfSimulator,
        "indivPhone" -> indivPhone,
        "indivFaceIfVerified" -> indivFaceIfVerified,
        "indivIpAddress" -> indivIpAddress,
        "indivDeviceMacAddress" -> indivDeviceMacAddress,
        "indivDeviceGeoLongitude" -> indivDeviceGeoLongitude,
        "indivDeviceGeoLatitude" -> indivDeviceGeoLatitude
      )
    }
  }
}
