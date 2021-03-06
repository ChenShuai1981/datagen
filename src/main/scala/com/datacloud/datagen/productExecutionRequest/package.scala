package com.datacloud.datagen


import org.scalacheck.Gen

package object productExecutionRequest {

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
  def genName: Gen[String] = Gen.oneOf("张三", "李四", "王五", "赵六")
  def genDeviceId: Gen[String] = Gen.identifier.map(s => "adid_" + s)
  def genIP: Gen[String] = for {
    d1 <- Gen.choose(0, 255)
    d2 <- Gen.choose(0, 255)
    d3 <- Gen.choose(0, 255)
    d4 <- Gen.choose(0, 255)
  } yield {
    s"$d1.$d2.$d3.$d4"
  }
  def genGPS: Gen[(String, String)] = for {
    latitude <- Gen.choose(28.0, 35.0)
    longitude <- Gen.choose(110.0, 120.0)
  } yield (latitude.toString, longitude.toString)

  def genDeviceIfProxy: Gen[String] = Gen.oneOf("true", "false")
  def genDeviceIfSimulator: Gen[String] = Gen.oneOf("true", "false")
  def genFaceIfVerified: Gen[String] = Gen.oneOf("true", "false")

  def genDeviceModelNo: Gen[String] = Gen.oneOf("oppo", "huawei", "samsung")
}
