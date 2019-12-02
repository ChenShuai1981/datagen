package com.datacloud.datagen.datasource

import com.datacloud.cybertron.protocol.avro.DataSourceExecutionCommonRecord
import com.datacloud.datagen.{AvroDataProducer, KafkaEnv}
import org.scalacheck.Gen

object DataSourceExecutionCommonRecordProducer extends App with KafkaEnv {
  val topicName = envPrefix + "UNION_DATASOURCE_EXECUTION_RECORD"
  val producer = new DataSourceExecutionCommonRecordProducer(topicName, bootstrapServers, schemaRegistryUrl, 600L, 1)
  producer.run()
}

class DataSourceExecutionCommonRecordProducer(topicName: String, bootstrapServers: String, schemaRegistryUrl: String, interval: Long, loop: Int)
  extends AvroDataProducer[DataSourceExecutionCommonRecord](topicName, bootstrapServers, schemaRegistryUrl, interval, loop) {

  def genData: Gen[DataSourceExecutionCommonRecord] = {
    for {
      tenantId <- Gen.oneOf(Seq(1883L))
//      region <- genRegion.map(_.name())
      region <- Gen.const("PRC")
      requestId <- Gen.identifier
      templateName <- Gen.oneOf(Seq("zhaoLvWang-historicalOrderInformation","hujin-threeElements","geerong-gzcb-cardowner","geo-phoneMonthlyTrafficLevelQuery","prc-corp-clientdata","bairong-education","geerong-tongdun-credit","bairong-total-loan","tongdun-bodyguard","Br_BadInfo","client-geerongMalaysia","juxinli-xiaomifen","client-shanmeng","jiayin-zzc","bairong-mobile-period","geo-assetsIndex-Z1003","client-geerongIndonesia","geerong-cloud-bodyguard","icekredit-huomou","jiayin-illegalcase","imassbank-clientdata","geo-fusionScoreIndicator","hujin-twoElements","bairong-score-cash-off","xingye-identity","geerong-indoModelV1","icekredit-mobilestatus","bairong-personal-credit","bairong-account-change","sloop-panbao","sync-test","tongdun-postloan-report","geerong-zhima-antifraud","tongdun-vietnam-mobifone","juxinli-miguan-report-v3","br-scoreconson","moxie-inter-facebook","jiayin-taxsuspend","icekredit-blacklist","bairong-mobile-consume","moxie-inland-accumulationfund","icekredit-mobileintime","advanceai-identity-check","bairong-pay-consumption","geerong-cloud-mobile-auth","bairong-real-name-authentication","geerong-cloud-social-security","geo-phoneNumberContactNumberInquiry","jiayin-companyGeneralInfo","tongdun-ktp","geerong-gzcb-blacklist","shanmeng-peopleBank-credit","geerong-zhima-credit","jiayin-riskdegree","tongDun-shanMengPreloanReport","tongdun-vietnam-vinaphone","51-HouseFunding","geerong-cloud-accumulation-fund","tongdun-preloan-report","bairong-location-verification","geerong-cloud-mobile-status","geerong-stats","imassbank-history","bairong-Tel-Id-Check","shijijiayuan-clientdata","jiayin-adminpenalty","jiayin-stressmonitor","geo-phoneNetworkTime","geo-mobilePhone3MonthsAverageConsumption","icekredit-mobileauth","geo-phoneNumberLastMarchDownTimeQuery","jiayin-inspectHandling","jiayin-tanzhimultiloan","xingye-peopleBank-credit","guangmu-clientdata","advanceai-multiplatform-detection","moxie-inland-carrier","geerong-cloud-mobile-intime","mojing-networkloan-check","moxie-mzreport-v2","geerong-creditFeedback","jiayin-loanprofile","bluepay-identify","bairong-score-cash-online","geerong-cloud-operator-report","moxie-inter-digi","xingye-zunyi-clientdata","geo-phoneNetworkStatus","bairong-credit-score","moxie-inter-googleplay","geo-phoneNearly3MonthsAverageTalkTime","sloop-panbao-common","geerong-tongdun-antifraud","geerong-cloud-risk-degree","advanceai-blacklist-check","tongdun-vietnam-viettel","geerong-indonesiaBlacklist","geerong-vzoom","icekredit-tiantong","jiayin-taxillegal","client-geerongVietnam","bairong-travel-list","geerong-cloud-finance-score","bairong-special-list","geerong-cloud-device-score","geo-telIdNameCheck","baihe-clientdata","nanshang_clientData","fuwei-clientdata","geo-mobilePhoneNumbeBlackListVerification","async-test","bairong-court-execution","zhaoLvWang-creditPlatformHistoryStats","xingye-baixiao-blacklist-detail","jiayin-litigation-personalSearch","moxie_socialsecurity","jiayin-taxhundred","geerong-gzcb-cardinfo","mojing-receivepay-check","client-zhaoLvWang","juxinli-carrier","jiayin-litigation-companySearch","geerong-cloud-preloan","geerong-gzcb-activity","bairong-apply-loan-intention","bairong-mobile-status","bairong-consumption","jiayin-educationinfo","geerong-cloud-blacklist","tongdun-identity-verification","jiayin-taxcredit"))
//      templateName <- Gen.const("client-geerongIndonesia")
      requestTime <- Gen.const(System.currentTimeMillis() - 300)
      responseResultTime <- Gen.const(System.currentTimeMillis())
      dataSourceId <- Gen.choose(1000L, 1200L)
      source <- Gen.oneOf(Seq("EXTERNAL", "INTERNAL"))
      status <- Gen.oneOf(Seq("SUCCESS", "FAILURE"))
    } yield {
      val executionMessage = new DataSourceExecutionCommonRecord()
      executionMessage.setTenantId(tenantId)
      executionMessage.setRegion(region)
      executionMessage.setRequestId(requestId)
      executionMessage.setTemplateName(templateName)
      executionMessage.setRequestTime(requestTime)
      executionMessage.setResponseResultTime(responseResultTime)
      executionMessage.setDataSourceId(dataSourceId)
      executionMessage.setSource(source)
      executionMessage.setStatus(status)

      executionMessage
    }
  }

  override def getKey(t: DataSourceExecutionCommonRecord): String = s"${t.getRequestId}"
}
