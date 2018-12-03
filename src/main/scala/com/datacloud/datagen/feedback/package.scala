package com.datacloud.datagen

import java.time.{Instant, LocalDateTime, ZoneId}

import com.datacloud.polaris.protocol.avro.{PaymentPlan, Region}
import org.scalacheck.Gen

package object feedback {

  def genRiskProcessId: Gen[Long] =  Gen.choose(1234560000L, 2345670000L)

  def genTerminal: Gen[String] = Gen.oneOf("GENERAL", "WEB", "IOS", "ANDROID")

  def genProductCode: Gen[String] = Gen.const("TEST")

  def genTenantId: Gen[Long] = Gen.oneOf(Array(29L))

  case class PersonalInfo(certNo: String, name: String, phone: String)
  val lastNames = Seq("赵","钱","孙","李","王","陈","毛","黄","谢")
  val phonePrefix = Seq(13, 15, 17, 18)

  def genRegion: Gen[Region] =
    Gen.oneOf(Seq(Region.INDONESIA))
  //    Gen.oneOf(Seq(Region.INDONESIA, Region.PRC, Region.MALAYSIA, Region.VIETNAM, Region.UNKNOWN))

  def genPersonalInfo: Gen[PersonalInfo] = for {
    i <- Gen.choose(1000, 9999)
    k = System.currentTimeMillis() + i
    kstr = k.toString
    certNo = s"36250$k"
    name <- Gen.oneOf(lastNames).map(_+k)
    phone <- Gen.oneOf(phonePrefix).map(_+kstr.substring(kstr.length-9))
  } yield PersonalInfo(certNo, name, phone)

  val zoneId = ZoneId.systemDefault()

  def genPlanRepayment(planRepaymentTime: Long): Gen[PaymentPlan] = for {
    planRepaymentCapital <- Gen.choose(1000.0, 3000.0)
    planRepaymentInterest <- Gen.choose(100.0, 300.0)
  } yield {
    PaymentPlan.newBuilder()
      .setPlanRepaymentTime(planRepaymentTime)
      .setPlanRepaymentCapital(planRepaymentCapital)
      .setPlanRepaymentInterest(planRepaymentInterest)
      .setPlanRepaymentAmount(planRepaymentCapital + planRepaymentInterest)
      .build()
  }

  def genPlanRepaymentList(start: Long, loanTerm: Int) = {
    Gen.sequence((1 to loanTerm).map(i => {
      val startLdt = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), zoneId)
      val planRepaymentTime = startLdt.plusDays(i).atZone(zoneId).toInstant.toEpochMilli
      genPlanRepayment(planRepaymentTime)
    }))
  }

  def genPenalty: Gen[(Option[Double], Option[Double])] = for {
    penaltyFixFee <- Gen.choose(1000.0d, 10000.0d)
    penaltyInterestRate <- Gen.choose(0.5d, 0.9d)
    pair <- Gen.oneOf((Some(penaltyFixFee), None), (None, Some(penaltyInterestRate)))
  } yield pair

  def genSurcharge: Gen[(Option[Double], Option[Double])] = for {
    surchargeFixFee <- Gen.choose(1000.0d, 10000.0d)
    surchargeRate <- Gen.choose(0.5d, 0.9d)
    pair <- Gen.oneOf((Some(surchargeFixFee), None), (None, Some(surchargeRate)))
  } yield pair
}
