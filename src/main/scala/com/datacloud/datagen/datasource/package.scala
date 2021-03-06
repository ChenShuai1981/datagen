package com.datacloud.datagen

import com.datacloud.polaris.protocol.avro.Region
import org.scalacheck.Gen

package object datasource {
  def genRegion: Gen[Region] =
  //    Gen.oneOf(Seq(Region.INDONESIA))
    Gen.oneOf(Seq(Region.INDONESIA, Region.PRC, Region.MALAYSIA, Region.VIETNAM, Region.UNKNOWN))

  def genDeviceId: Gen[Option[String]] = Gen.option(Gen.identifier.map(s => "adid_" + s))

  def genBankNo: Gen[Option[String]] = Gen.option(Gen.choose(1, 100).map(id => s"bankno_$id"))

  def genName: Gen[String] = Gen.oneOf("张三", "李四", "王五", "赵六")

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


}
