//package com.datacloud.datagen.cybertron
//
//import java.util.Date
//
//import com.datacloud.cybertron.service.ds.msg.execution.{TongdunInput, TongdunOutput, TongdunPreloan}
//import com.datacloud.datagen.AvroDataProducer
//import org.scalacheck.Gen
//
//import scala.collection.JavaConversions._
//
//object DSTongdunPreloanProducer extends App {
//  //  val topicName = "preprod_RISK_INVOCATION_HISTORY"
//  //  val bootstrapServers = "10.12.0.6:9092"
//  //  val schemaRegistryUrl = "http://10.12.0.6:8081"
//
//  val topicName = "datasource-tongdun-preloan-report"
//  val bootstrapServers = "ambari-agent4.sit.geerong.com:9092"
//  val schemaRegistryUrl = "http://ambari-agent4.sit.geerong.com:8081"
//
//  val producer = new DSTongdunPreloanProducer(topicName, bootstrapServers, schemaRegistryUrl, 60L, 1)
//  producer.run()
//}
//
//class DSTongdunPreloanProducer (topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
//  extends AvroDataProducer[TongdunPreloan](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {
//
//  def genData: Gen[TongdunPreloan] = for {
//    requestId <- Gen.identifier
//    upstreamId <- Gen.identifier
//    upstreamRequestId <- Gen.identifier
//    downstreamRequestId <- Gen.option(Gen.identifier)
//    templateName <- Gen.const("templateName")
//    requestTime <- Gen.const(System.currentTimeMillis())
//    requestResultTime <- Gen.const(requestTime + 20)
//    responseResultTime <- Gen.const(requestResultTime + 300)
//    dataSourceId <- Gen.const(1234L)
//    expiredTime <- Gen.const(responseResultTime + 3600 * 1000 * 24 * 7)
//    source <- Gen.const("source")
//    status <- Gen.const("success")
//    comment <- Gen.option(Gen.const("comment"))
//    input <- genInput
//    output <- genOutput
//  } yield {
//    TongdunPreloan(requestId, upstreamId, upstreamRequestId, downstreamRequestId,
//      templateName, requestTime, requestResultTime, responseResultTime, dataSourceId,
//      expiredTime, source, status, comment, input, output)
//  }
//
//  private def genInput = for {
//    indivName <- Gen.option(Gen.identifier)
//    indivID <- Gen.option(Gen.numStr)
//    indivPhone <- Gen.option(Gen.numStr)
//  } yield TongdunInput(indivName, indivID, indivPhone)
//
//  private def genOutput = for {
//    td_antifraudScore <- Gen.option(Gen.choose(20L, 90L))
//    td_finalDecision <- Gen.option(Gen.oneOf(Seq("accept", "reject", "review")))
//    td_applyTime <- Gen.option(Gen.const("2018-05-02 11:23:56"))
//    td_reportTime <- Gen.option(Gen.const("2018-05-02 12:12:34"))
//    td_deviceInfo_deviceId <- Gen.option(Gen.identifier)
//    td_geoIp_ip <- Gen.option(Gen.const("10.12.0.161"))
//    td_geoIp_county <- Gen.option(Gen.const("Shanghai"))
//    td_geoIp_country <- Gen.option(Gen.const("China"))
//    td_geoIp_city <- Gen.option(Gen.const("Shanghai"))
//    td_geoIp_lip <- Gen.option(Gen.const("lip"))
//    td_geoIp_isp <- Gen.option(Gen.const("CNU"))
//    td_geoIp_province <- Gen.option(Gen.const("Shanghai"))
//    td_geoIp_latitude <- Gen.option(Gen.const(BigDecimal("30.02139821")))
//    td_geoIp_longitude <- Gen.option(Gen.const(BigDecimal("120.17233232")))
//    td_geoTrueip_ip <- Gen.option(Gen.const("10.12.0.161"))
//    td_geoTrueip_county <- Gen.option(Gen.const("Shanghai"))
//    td_geoTrueip_country <- Gen.option(Gen.const("China"))
//    td_geoTrueip_city <- Gen.option(Gen.const("Shanghai"))
//    td_geoTrueip_lip <- Gen.option(Gen.const("lip"))
//    td_geoTrueip_isp <- Gen.option(Gen.const("CNU"))
//    td_geoTrueip_province <- Gen.option(Gen.const("Shanghai"))
//    td_geoTrueip_latitude <- Gen.option(Gen.const(BigDecimal("30.02139821")))
//    td_geoTrueip_longitude <- Gen.option(Gen.const(BigDecimal("120.17233232")))
//    td_addressDetect_idCardAddress <- Gen.option(Gen.const("马家村"))
//    td_addressDetect_trueIpAddress <- Gen.option(Gen.const("马家村"))
//    td_addressDetect_wifiAddress <- Gen.option(Gen.const("马家村"))
//    td_addressDetect_cellAddress <- Gen.option(Gen.const("马家村"))
//    td_addressDetect_bankCardAddress <- Gen.option(Gen.const("马家村"))
//    td_addressDetect_mobileAddress <- Gen.option(Gen.const("马家村"))
//    td_riskItems_0_itemId <- Gen.option(Gen.const(1234L))
//    td_riskItems_0_itemName <- Gen.option(Gen.const("itemName"))
//    td_riskItems_0_riskLevel <- Gen.option(Gen.const("high"))
//    td_riskItems_0_group <- Gen.option(Gen.const("group"))
//    td_riskItems_0_itemDetail_discreditTimes <- Gen.option(Gen.containerOf(Gen.choose(1L, 5L)))
//    td_riskItems_0_itemDetail_overdueDetails_0_overdueAmountRange <- Gen.option(Gen.containerOfN(3, Gen.oneOf(Seq("1000", "1500", "2000"))))
//    td_riskItems_0_itemDetail_overdueDetails_0_overdueCount <- Gen.option(Gen.containerOfN(3, Gen.choose(1L, 5L)))
//    td_riskItems_0_itemDetail_overdueDetails_0_overdueDayRange <- Gen.option(Gen.containerOfN(3, Gen.oneOf(Seq("1", "30"))))
//    td_riskItems_0_itemDetail_overdueDetails_0_overdueTime <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.choose(System.currentTimeMillis() - 30 * 60 * 1000L, System.currentTimeMillis()).map(new Date(_)))))
//    td_riskItems_0_itemDetail_platformCount <- Gen.option(Gen.choose(1L, 5L))
//    td_riskItems_0_itemDetail_platformDetail_0_x <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_platformDetail_0_x"))))
//    td_riskItems_0_itemDetail_platformDetailDimension_0_count <- Gen.option(Gen.containerOfN(3, Gen.choose(1L, 5L)))
//    td_riskItems_0_itemDetail_platformDetailDimension_0_detail_0_x <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_platformDetailDimension_0_detail_0_x")))))
//    td_riskItems_0_itemDetail_platformDetailDimension_0_dimension <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_platformDetailDimension_0_dimension"))))
//    td_riskItems_0_itemDetail_highRiskAreas_0_x <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_highRiskAreas_0_x"))))
//    td_riskItems_0_itemDetail_hitListDatas_0_x <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_hitListDatas_0_x"))))
//    td_riskItems_0_itemDetail_fraudType <- Gen.option(Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_fraudType")))
//    td_riskItems_0_itemDetail_frequencyDetailList_0_data_0_x <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_frequencyDetailList_0_data_0_x")))))
//    td_riskItems_0_itemDetail_frequencyDetailList_0_detail <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_frequencyDetailList_0_detail"))))
//    td_riskItems_0_itemDetail_crossFrequencyDetailList_0_data <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_crossFrequencyDetailList_0_data"))))
//    td_riskItems_0_itemDetail_crossFrequencyDetailList_0_detail <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_crossFrequencyDetailList_0_detail"))))
//    td_riskItems_0_itemDetail_crossEventDetailList_0_data <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_crossEventDetailList_0_data"))))
//    td_riskItems_0_itemDetail_crossEventDetailList_0_detail <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_crossEventDetailList_0_detail"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_description <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_description"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_fraudType <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_fraudType"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_hitTypeDisplayname <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_hitTypeDisplayname"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_type <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_type"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fraudType <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fraudType"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyIdNumber <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyIdNumber"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyName <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyName"))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_fraudType <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_fraudType")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_name <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_name")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_age <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_age")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_gender <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_gender")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_province <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_province")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_filingTime <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_filingTime")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_courtName <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_courtName")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionDepartment <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionDepartment")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_duty <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_duty")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_situation <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_situation")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_discreditDetail <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_discreditDetail")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionBase <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionBase")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_caseNumber <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_caseNumber")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionNumber <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionNumber")))))
//    td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionStatus <- Gen.option(Gen.containerOfN(3, Gen.containerOfN(3, Gen.containerOfN(3, Gen.const("td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionStatus")))))
//  } yield {
//    TongdunOutput(td_antifraudScore,
//      td_finalDecision,
//      td_applyTime,
//      td_reportTime,
//      td_deviceInfo_deviceId,
//      td_geoIp_ip,
//      td_geoIp_county,
//      td_geoIp_country,
//      td_geoIp_city,
//      td_geoIp_lip,
//      td_geoIp_isp,
//      td_geoIp_province,
//      td_geoIp_latitude,
//      td_geoIp_longitude,
//      td_geoTrueip_ip,
//      td_geoTrueip_county,
//      td_geoTrueip_country,
//      td_geoTrueip_city,
//      td_geoTrueip_lip,
//      td_geoTrueip_isp,
//      td_geoTrueip_province,
//      td_geoTrueip_latitude,
//      td_geoTrueip_longitude,
//      td_addressDetect_idCardAddress,
//      td_addressDetect_trueIpAddress,
//      td_addressDetect_wifiAddress,
//      td_addressDetect_cellAddress,
//      td_addressDetect_bankCardAddress,
//      td_addressDetect_mobileAddress,
//      td_riskItems_0_itemId,
//      td_riskItems_0_itemName,
//      td_riskItems_0_riskLevel,
//      td_riskItems_0_group,
//      td_riskItems_0_itemDetail_discreditTimes,
//      td_riskItems_0_itemDetail_overdueDetails_0_overdueAmountRange,
//      td_riskItems_0_itemDetail_overdueDetails_0_overdueCount,
//      td_riskItems_0_itemDetail_overdueDetails_0_overdueDayRange,
//      td_riskItems_0_itemDetail_overdueDetails_0_overdueTime,
//      td_riskItems_0_itemDetail_platformCount,
//      td_riskItems_0_itemDetail_platformDetail_0_x,
//      td_riskItems_0_itemDetail_platformDetailDimension_0_count,
//      td_riskItems_0_itemDetail_platformDetailDimension_0_detail_0_x,
//      td_riskItems_0_itemDetail_platformDetailDimension_0_dimension,
//      td_riskItems_0_itemDetail_highRiskAreas_0_x,
//      td_riskItems_0_itemDetail_hitListDatas_0_x,
//      td_riskItems_0_itemDetail_fraudType,
//      td_riskItems_0_itemDetail_frequencyDetailList_0_data_0_x,
//      td_riskItems_0_itemDetail_frequencyDetailList_0_detail,
//      td_riskItems_0_itemDetail_crossFrequencyDetailList_0_data,
//      td_riskItems_0_itemDetail_crossFrequencyDetailList_0_detail,
//      td_riskItems_0_itemDetail_crossEventDetailList_0_data,
//      td_riskItems_0_itemDetail_crossEventDetailList_0_detail,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_description,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_fraudType,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_hitTypeDisplayname,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_type,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fraudType,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyIdNumber,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_fuzzyDetailHits_0_fuzzyName,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_fraudType,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_name,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_age,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_gender,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_province,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_filingTime,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_courtName,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionDepartment,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_duty,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_situation,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_discreditDetail,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionBase,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_caseNumber,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionNumber,
//      td_riskItems_0_itemDetail_namelistHitDetails_0_courtDetails_0_executionStatus)
//  }
//
//}
