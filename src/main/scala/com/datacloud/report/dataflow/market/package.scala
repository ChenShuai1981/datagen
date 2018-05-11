package com.datacloud.report.dataflow

import org.scalacheck.Gen

package object market {

  case class MarketDecisionResult(tenantId: String, // 租户ID
                                  marketProcessId: String, // 营销进件流程所颁发的ID
                                  marketActivityId: String, // 营销活动ID
                                  decisionStrategyId: String, // 营销决策ID
                                  marketContentTemplateId: String, // 营销内容模板ID
                                  creditCardNo: String, // 信用卡号
                                  cardOwnerIndivID: String, // 持卡客户身份证号
                                  cardOwnerName: String, // 持卡客户姓名
                                  cardOwnerPhone: String, // 持卡客户手机号
                                  cardOwnerEmail: String, // 持卡客户邮箱
                                  marketWay: String, // 营销方式
                                  transactionTime: Long, // 交易时间
                                  marketDecisionStrategy: String // 营销策略结果
                                 )

  case class GainActivityRightEvent(tenantId: String,
                                    marketActivityId: String,
                                    decisionStrategyId: String,
                                    cardOwnerIndivD: String,
                                    cardOwnerName: String,
                                    cardOwnerPhone: String,
                                    cardOwnerEmail: String,
                                    creditCardNo: String,
                                    gainActivityRightTime: Long)

  case class PersonalInfo(certNo: String, name: String, phone: String, email: String, creditCardNo: String)


  val customers = (1000 to 9999).map(s => {
    // 1525945209741
    val k = System.currentTimeMillis() + s
    val kstr = k.toString
    PersonalInfo(s"36250$k", s"客户$k", kstr.substring(kstr.length-11), s"$k@mail.com", s"2219$k")
  })

  def genPersonalInfo: Gen[PersonalInfo] = Gen.oneOf(customers)

  def genBizId: Gen[Map[String, Seq[String]]] = Gen.const(Map(
    "500" -> Seq("500"),
    "11128123" -> Seq("237812368213", "212312389712", "231273981273"),
    "11128124" -> Seq("123891723912", "239131983213"),
    "11128125" -> Seq("129319231983", "382136127332", "238713618321", "123891238682")
  ))

  def genMarketDecisionStrategy: Gen[String] = Gen.oneOf(Seq("pass", "reject"))

  def genTransactionTime: Gen[Long] = {
    val now = System.currentTimeMillis()
    val start = now - 2*3600*24*1000
    Gen.choose(start, now)
  }
}
