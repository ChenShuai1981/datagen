package com.datacloud.datagen

import java.time.{Instant, LocalDateTime, ZoneId}

import org.scalacheck.Gen

package object clientdata {

  def genCertNo: Gen[String] = for {
    region <- Gen.choose(100000, 999999).map(_.toString) // 6
    year <- Gen.choose(1930, 2000).map(_.toString) // 4
    month <- Gen.choose(1, 12).map(m => if (m < 10) "0"+m else m.toString) // 2
    day <- Gen.choose(1, 30).map(m => if (m < 10) "0"+m else m.toString) // 2
    suffix <- Gen.choose(1000, 9999).map(_.toString) // 4
  } yield {
    region + year + month + day + suffix
  }

  def genPhone: Gen[String] = Gen.choose(13000000000L, 18900000000L).map(_.toString)
  def genRelation: Gen[String] = Gen.oneOf("PARENT", "CLASSMATE", "SPOUSE")
  def genName: Gen[String] = Gen.oneOf("张三", "李四", "王五", "赵六")
  def genBankNo: Gen[Option[String]] = Gen.option(Gen.choose(1, 100).map(id => s"bankno_$id"))
  def genDeviceId: Gen[Option[String]] = Gen.option(Gen.identifier.map(s => "adid_" + s))
  def genIP: Gen[String] = for {
    d1 <- Gen.choose(0, 255)
    d2 <- Gen.choose(0, 255)
    d3 <- Gen.choose(0, 255)
    d4 <- Gen.choose(0, 255)
  } yield {
    s"$d1.$d2.$d3.$d4"
  }
  def genGPS: Gen[(Double, Double)] = for {
      latitude <- Gen.choose(28.0, 35.0)
      longitude <- Gen.choose(110.0, 120.0)
  } yield (latitude, longitude)

  def genSmsMessage: Gen[(String, String, LocalDateTime, String, String)] = for {
    address <- Gen.choose(130923271897L, 1332389473298L).map(_.toString)
    body <- Gen.oneOf("uang kadf adwerop", "abcdefgakfjawrwer", "akdfhsdf23", "293osfsf3204")
    date <- Gen.const(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime())
    status <- Gen.oneOf("STATUS_COMPLETE", "STATUS_PENDING", "STATUS_FAILED", "STATUS_UNKNOWN", "STATUS_NONE")
    ttype <- Gen.oneOf("MESSAGE_TYPE_ALL", "MESSAGE_TYPE_INBOX", "MESSAGE_TYPE_SENT", "MESSAGE_TYPE_DRAFT", "MESSAGE_TYPE_OUTBOX", "MESSAGE_TYPE_FAILED", "MESSAGE_TYPE_QUEUED", "MESSAGE_TYPE_UNKNOWN")
  } yield (address, body, date, status, ttype)

  def genSmsMessages: Gen[List[(String, String, LocalDateTime, String, String)]] = Gen.choose(3, 10).flatMap(k => Gen.listOfN(k, genSmsMessage))
}
