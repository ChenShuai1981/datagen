package com.datacloud.datagen

import java.util.{Calendar, Date}

import com.datacloud.polaris.protocol.avro._
import org.scalacheck.Gen

import scala.collection.mutable

package object history {
  def genHitRule: Gen[HitRule] = for {
    ruleDescription <- Gen.const("ruleDescription")
    (ruleId, ruleName) <- Gen.choose(101505L, 101515L).map(i => (i, "rule-"+i))
    ruleTemplateId <- Gen.choose(4567L, 5678L)
  } yield {
    val hitRule = new HitRule()
    hitRule.setRuleDescription(ruleDescription)
    hitRule.setRuleId(199L)
    hitRule.setRuleName("rule-199")
    hitRule.setRuleTemplateId(ruleTemplateId)
    hitRule
  }

  def genAntifraudHitRuleSet: Gen[AntifraudHitRuleSet] = for {
    decision <- genDecision
    returnMsg <- Gen.const("returnMsg")
    ruleSetId <- Gen.choose(2000L, 3000L)
    ruleSetName <- Gen.identifier.map("ruleSetName_" + _)
    ruleSetMode <- genRuleSetMode
    hitRules <- Gen.choose(1, 3).flatMap(size => Gen.sequence((1 to size).map(_ => genHitRule)))
  } yield {
    val antifraudHitRuleSet = new AntifraudHitRuleSet()
    antifraudHitRuleSet.setDecision(Decision.reject)
    antifraudHitRuleSet.setReturnMsg(returnMsg)
    antifraudHitRuleSet.setRuleSetName("ruleset-2002")
    antifraudHitRuleSet.setRuleSetId(2002L)
    antifraudHitRuleSet.setRuleSetMode(RuleSetMode.valueOf(ruleSetMode.name()))
    antifraudHitRuleSet.setHitRules(hitRules)

    antifraudHitRuleSet
  }

  def genAntifraudDetail: Gen[AntifraudDetail] = for {
    antifraudDecision <- genDecision
    decisionReason <- Gen.const("decisionReason")
    hitRuleSets <- Gen.choose(1, 3).flatMap(size => Gen.sequence((1 to size).map(_ => genAntifraudHitRuleSet)))
  } yield {
    val antifraudDetail = new AntifraudDetail()
    antifraudDetail.setAntifraudDecision(antifraudDecision)
    antifraudDetail.setDecisionReason(decisionReason)
    antifraudDetail.setHitRuleSets(hitRuleSets)
//    antifraudDetail.setHitRuleSets(new java.util.ArrayList())

    antifraudDetail
  }

  def genAdmissionDetail: Gen[AdmissionDetail] = for {
    admissionDecision <- genDecision
    decisionReason <- Gen.const("decisionReason")
    hitRuleSets <- Gen.choose(1, 3).flatMap(size => Gen.sequence((1 to size).map(_ => genAntifraudHitRuleSet)))
  } yield {
    val admissionDetail = new AdmissionDetail()
    admissionDetail.setAdmissionDecision(admissionDecision)
    admissionDetail.setDecisionReason(decisionReason)
    admissionDetail.setHitRuleSets(hitRuleSets)
    //    admissionDetail.setHitRuleSets(new java.util.ArrayList())

    admissionDetail
  }

  def genCreditDetail: Gen[CreditDetail] = for {
    amount <- Gen.choose(1000.0, 100000.0)
    creditDecision <- genDecision
    rateType <- genRateType
    rateValue <- Gen.choose(0.12, 0.76)
    compoundPeriod <- Gen.choose(1, 24)
    creditStartDate <- genCreditStartDate
  } yield {
    val creditDetail = new CreditDetail()
    creditDetail.setAmount(amount)
    creditDetail.setCompoundPeriod(compoundPeriod)
    creditDetail.setCreditDecision(creditDecision)
    creditDetail.setCreditStartDate(creditStartDate.getTime)
    creditDetail.setCreditEndDate(creditStartDate.getTime + 1000 * 60 * 60 * 24 * 30 * 6)
    if (rateType.isDefined) {
      creditDetail.setRateType(rateType.get)
    }
    creditDetail.setRateValue(rateValue)

    creditDetail
  }

  def genCreditStartDate: Gen[Date] = for {
    days <- Gen.choose(1, 30)
  } yield {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_MONTH, -days)
    cal.getTime
  }

  def genDecision: Gen[Decision] = Gen.oneOf(Decision.accept, Decision.reject, Decision.review)

  def genRateType: Gen[Option[RateType]] = Gen.option(Gen.oneOf(RateType.absolute, RateType.relative, RateType.multiple))

  def genRiskLevel: Gen[RiskLevel] = Gen.oneOf(RiskLevel.low, RiskLevel.medium, RiskLevel.high)

  def genRuleSetMode: Gen[RuleSetMode] = Gen.oneOf(RuleSetMode.firstMode, RuleSetMode.weightMode, RuleSetMode.worstMode)

  def genAge: Gen[Option[Int]] = Gen.option(Gen.choose(16, 70))

  def genGender: Gen[Option[String]] = Gen.option(Gen.oneOf("male", "female"))

  def genName: Gen[String] = Gen.oneOf("张三", "李四", "王五", "赵六")

  def genCertNo: Gen[String] = Gen.const("450331199511070613") // Gen.choose(312039129372189999L, 578129129372189999L).map(_.toString)

  def genPhone: Gen[Option[String]] = Gen.option(Gen.const("18887749973"))// Gen.option(Gen.choose(13000000000L, 18900000000L).map(_.toString))

  //  def genGPS: Gen[Option[(Double, Double)]] = Gen.option(for {
  //    latitude <- Gen.choose(28.0, 35.0)
  //    longitude <- Gen.choose(110.0, 120.0)
  //  } yield (latitude, longitude))

  def genGPS: Gen[Option[(Double, Double)]] = Gen.option(Gen.const(31.213981237,120.21398123))

  //  def genIP: Gen[Option[String]] = Gen.option(for {
  //    d1 <- Gen.oneOf(58,59,60,61,121,122,124,125,202)
  //    d2 <- Gen.choose(30, 240)
  //    d3 <- Gen.choose(0, 255)
  //    d4 <- Gen.choose(0, 255)
  //  } yield {
  //    s"$d1.$d2.$d3.$d4"
  //  })

  def genIP: Gen[Option[String]] = Gen.option(Gen.const("115.238.190.238"))

  //  def genIP: Gen[String] = for {
  //    d1 <- Gen.oneOf(58,59,60,61,121,122,124,125,202)
  //    d2 <- Gen.choose(30, 240)
  //    d3 <- Gen.choose(0, 255)
  //    d4 <- Gen.choose(0, 255)
  //  } yield {
  //    s"$d1.$d2.$d3.$d4"
  //  }

  def genDeviceId: Gen[Option[String]] = Gen.option(Gen.identifier)

  def genBankNo: Gen[Option[String]] = Gen.option(Gen.choose(1, 100).map(id => s"bankno_$id"))

  def genInput: Gen[Map[String, String]] = for {
    name <- genName
    certNo <- genCertNo
    phone <- genPhone
    ipOption <- genIP
    gpsOption <- genGPS
    gender <- genGender
    age <- genAge
    deviceId <- genDeviceId
    bankNo <- genBankNo
  } yield {
    var map = mutable.Map[String, String]()
    map += ("indivName__ROLE__APPLICANT" -> name)
    map += ("indivID__ROLE__APPLICANT" -> certNo)
    if (ipOption.isDefined) {
      map += ("indivIpAddress__ROLE__APPLICANT" -> ipOption.get)
    }
    if (gpsOption.isDefined) {
      map += ("indivDeviceGeoLatitude__ROLE__APPLICANT" -> gpsOption.get._1.toString)
      map += ("indivDeviceGeoLongitude__ROLE__APPLICANT" -> gpsOption.get._2.toString)
    }
    map += ("indivPhone__ROLE__APPLICANT" -> "18887749973")
//    if (phone.isDefined) map += ("indivPhone__ROLE__APPLICANT" -> phone.get)
    if (gender.isDefined) map += ("indivGender__ROLE__APPLICANT" -> gender.get)
    if (age.isDefined) map += ("indivAge__ROLE__APPLICANT" -> age.get.toString)
    if (deviceId.isDefined) map += ("clientData_deviceInfo_generalDeviceId" -> deviceId.get.toString)
    if (bankNo.isDefined) map += ("indivBankNo__ROLE__APPLICANT" -> bankNo.get.toString)

    map.toMap
  }
}
