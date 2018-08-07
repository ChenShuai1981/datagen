package com.datacloud.datagen

import org.scalacheck.Gen

import scala.collection.mutable

package object market {

  case class Appendix(gzcbCreditCardType: String,
                      DECISION_STREAM_TENANT_ID: Long,
                      transactionType: String,
                      transactionContext: String,
                      DECISION_STREAM_EXECUTION_ID: Long,
                      rejectReason: String,
                      transactionAmount: String
                     )

  case class MarketDecisionResult(tenantId: String, // 租户ID
                                  marketProcessId: String, // 营销进件流程所颁发的ID
                                  marketActivityId: String, // 营销活动ID
                                  decisionStrategyId: String, // 营销决策ID
                                  strategyDeployId: String, // 策略部署ID
                                  creditCardNo: String, // 信用卡号
                                  cardOwnerIndivID: String, // 持卡客户身份证号
                                  cardOwnerName: String, // 持卡客户姓名
                                  cardOwnerPhone: String, // 持卡客户手机号
                                  cardOwnerEmail: String, // 持卡客户邮箱
                                  marketWay: String, // 营销方式
                                  transactionTime: Long, // 交易时间
                                  marketDecisionStrategy: String, // 营销策略结果
                                  appendix: Appendix
                                 )

  case class GainActivityRightEvent(tenantId: String,
                                    marketActivityId: String,
                                    decisionStrategyId: String,
                                    cardOwnerIndivID: String,
                                    cardOwnerName: String,
                                    cardOwnerPhone: String,
                                    cardOwnerEmail: String,
                                    creditCardNo: String,
                                    gainActivityRightTime: Long,
                                    isPerCardOwnerIndivID: Boolean)

  case class UnionPayTrx(
                          // 交易账号
                          trxAccount: String,

                          // 交易金额
                          trxAmount: BigDecimal,

                          // 交易描述
                          trxDescription: String,

                          // 交易类型
                          trxType: String,

                          // 交易时间
                          trxTime: Long)

  case class PersonalInfo(certNo: String, name: String, phone: String, email: String, creditCardNo: String)

  case class MarketingRequest(tenantId: Long, marketActivityId: Long, decisionStrategyId: Long,
                              strategyDeployId: Long, occurTime: Long, data: Map[String, String])

  val customers = (1000 to 9999).map(s => {
    // 1525945209741
    val k = System.currentTimeMillis() + s
    val kstr = k.toString
    PersonalInfo(s"36250$k", s"$k", kstr.substring(kstr.length-11), s"$k@mail.com", s"2219$k")
  })

  val lastNames = Seq("赵","钱","孙","李","王","陈","毛","黄","谢")

  def genPersonalInfo: Gen[PersonalInfo] = for {
    personalInfo <- Gen.oneOf(customers)
    lastName <- Gen.oneOf(lastNames)
  } yield {
    personalInfo.copy(name = lastName + personalInfo.name)
  }

  def genBizId: Gen[Map[String, Seq[String]]] = Gen.const(Map(
    "519" -> Seq("362607327850790919")
//    "507" -> Seq("228312837507"),
//    "123" -> Seq("237812368213", "212312389712", "231273981273"),
//    "124" -> Seq("123891723912", "239131983213"),
//    "125" -> Seq("129319231983", "382136127332", "238713618321", "123891238682")
  ))

  def genMarketDecisionStrategy: Gen[String] = Gen.oneOf(Seq("pass", "reject"))

  def genTransactionTime: Gen[Long] = {
    val now = System.currentTimeMillis()
    val start = now - 2*3600*24*1000
    Gen.choose(start, now)
  }

  def genName: Gen[String] = Gen.oneOf("张三", "李四", "王五", "赵六")

  def genCertNo: Gen[String] = Gen.const("450331199511070613") // Gen.choose(312039129372189999L, 578129129372189999L).map(_.toString)

  def genPhone: Gen[Option[String]] = Gen.option(Gen.const("18887749973"))// Gen.option(Gen.choose(13000000000L, 18900000000L).map(_.toString))

  def genExtraData: Gen[Map[String, String]] = for {
    name <- genName
    certNo <- genCertNo
    phone <- genPhone
  } yield {
    var map = mutable.Map[String, String]()
    map += ("indivName__ROLE__APPLICANT" -> name)
    map += ("indivID__ROLE__APPLICANT" -> certNo)
    if (phone.isDefined) map += ("indivPhone__ROLE__APPLICANT" -> phone.get)
    map.toMap
  }
}
