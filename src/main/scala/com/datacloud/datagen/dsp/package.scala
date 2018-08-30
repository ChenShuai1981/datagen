package com.datacloud.datagen

import com.datacloud.dsp.avro._
import org.scalacheck.Gen
import scala.collection.JavaConversions._

package object dsp {

//  case class BannerObj(w: Option[Int],
//                             h: Option[Int],
//                             pos: Option[Int])
//
//  case class VideoObj(mimes: Seq[String],
//                            w: Int,
//                            h: Int,
//                            pos: Option[Int],
//                            mindur: Option[Int],
//                            maxdur: Option[Int],
//                            minbitrate: Option[Int],
//                            maxbitrate: Option[Int])
//
//  case class ImpExtObj(title: Option[String],
//                             keywords: Option[String],
//                             cat: Option[String])
//
//  case class AppObj(id: String,
//                          ver: Option[String],
//                          bundle: Option[String])
//
//  case class DeviceObj(ip: String,
//                             ipv6: Option[String],
//                             devtype: Int,
//                             lang: Option[String],
//                             make: Option[String],
//                             model: Option[String],
//                             os: Option[String],
//                             osv: Option[String],
//                             conntype: Int,
//                             didmd5: Option[String],
//                             didsha1: Option[String],
//                             dpidmd5: Option[String],
//                             dpidsha1: Option[String],
//                             macmd5: Option[String],
//                             macsha1: Option[String],
//                             idfa: Option[String],
//                             idfamd5: Option[String],
//                             idfasha1: Option[String],
//                             ort: Option[Int])
//
//  case class GeoObj(lat: Option[Double],
//                          lon: Option[Double],
//                          country: Option[String],
//                          prov: Option[String],
//                          city: Option[String],
//                          street: Option[String],
//                          `type`: Option[Int],
//                          accu: Option[Int])
//
//  case class ImpObj(id: String,
//                          tagid: String,
//                          banner: Option[BannerObj],
//                          video: Option[VideoObj],
//                          ext: Option[ImpExtObj])
//
//  case class UserObj(id: String,
//                           gender: Option[String],
//                           yob: Option[Int],
//                           keywords: Option[String],
//                           ext: Option[Map[String, String]])
//
//  case class BidRequestObj(id: String,
//                                 imp: Seq[ImpObj],
//                                 app: AppObj,
//                                 device: DeviceObj,
//                                 user: UserObj,
//                                 tmax: Option[Int],
//                                 bcat: Option[Seq[String]])
//
//  case class BidResponseExtObj(title: Option[String],
//                                     desc: Option[String],
//                                     action: Int,
//                                     images: Array[String],
//                                     videos: Array[String],
//                                     turl: Option[String],
//                                     clkurl: String,
//                                     impurl: String,
//                                     imptrackers: Array[String],
//                                     clktrackers: Array[String])
//
//  case class BidResponseObj(id: String,
//                                  bidid: String,
//                                  adm: Option[String],
//                                  cat: Option[String],
//                                  ext: BidResponseExtObj)

  def genUserId: Gen[String] = Gen.choose(10000, 99999).map(n => s"userid_$n")

  def genUserObj: Gen[UserObj] = for {
    id <- Gen.uuid.map(_.toString)
    gender <- Gen.oneOf(Seq("M", "F", "O"))
    yob <- Gen.choose(1970, 2000)
    keywords <- Gen.oneOf("food", "travel", "music", "sports")
    ext <- Gen.const(Map.empty[String, String])
  } yield {
    new UserObj(id, gender, yob, keywords, ext)
  }

  def genBannerObj: Gen[BannerObj] = for {
    (w, h) <- Gen.oneOf(Seq((50, 100), (80, 150), (32, 48)))
    pos <- Gen.choose(0, 7)
  } yield {
    new BannerObj(w, h, pos)
  }

  def genVideoObj: Gen[VideoObj] = for {
    mimes <- Gen.someOf(Seq("avi", "mkv", "mp4"))
    (w, h) <- Gen.oneOf(Seq((50, 100), (80, 150), (32, 48)))
    pos <- Gen.choose(0, 7)
    mindur <- Gen.choose(1000, 3000)
    maxdur <- Gen.choose(10000, 30000)
    minbitrate <- Gen.choose(1000, 3000)
    maxbitrate <- Gen.choose(10000, 30000)
  } yield {
    new VideoObj(mimes, w, h, pos, mindur, maxdur, minbitrate, maxbitrate)
  }

  def genImpExtObj: Gen[ImpExtObj] = for {
    title <- Gen.oneOf(Seq("hello world"))
    keywords <- Gen.oneOf(Seq("women", "sports", "tv", "music"))
    cat <- Gen.someOf(Gen.choose(1, 10).map(n => s"IAB_$n"), Gen.choose(11, 20).map(n => s"IAB_$n"))
  } yield {
    new ImpExtObj(title, keywords, cat)
  }

  def genAppObj: Gen[AppObj] = for {
    id <- genChannelId
    ver <- Gen.choose(1, 100).map(_.toString)
    bundle <- Gen.const("com..app")
  } yield {
    new AppObj(id, ver, bundle)
  }

  def genUA: Gen[String] = Gen.oneOf(Seq("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"))

  def genIP: Gen[String] = for {
      d1 <- Gen.choose(0, 255)
      d2 <- Gen.choose(0, 255)
      d3 <- Gen.choose(0, 255)
      d4 <- Gen.choose(0, 255)
    } yield {
      s"$d1.$d2.$d3.$d4"
    }

  def genDeviceObj: Gen[DeviceObj] = for {
    ip <- genIP
    ipv6 <- Gen.const("2001:0DB8:0000:0023:0008:0800:200C:417A")
    devtype <- Gen.choose(1, 7)
    lang <- Gen.oneOf(Seq("zh", "en", "jp"))
    make <- Gen.oneOf(Seq("huawei", "xiaomi", "apple", "vivo"))
    model <- Gen.oneOf(Seq("iPhone", "Rongyao", "Mi", "Nexus"))
    os <- Gen.oneOf(Seq("android", "ios"))
    osv <- Gen.oneOf(Seq("1.1", "3.2", "5.0"))
    conntype <- Gen.choose(0, 6)
    didmd5 <- Gen.uuid.map(_.toString)
    didsha1 <- Gen.uuid.map(_.toString)
    dpidmd5 <- Gen.uuid.map(_.toString)
    dpidsha1 <- Gen.uuid.map(_.toString)
    macmd5 <- Gen.uuid.map(_.toString)
    macsha1 <- Gen.uuid.map(_.toString)
    idfa <- Gen.uuid.map(_.toString)
    idfamd5 <- Gen.uuid.map(_.toString)
    idfasha1 <- Gen.uuid.map(_.toString)
    ort <- Gen.choose(0, 1)
  } yield {
    new DeviceObj(
      ip, ipv6, devtype, lang, make, model, os, osv, conntype,
      didmd5, didsha1, dpidmd5, dpidsha1,
      macmd5, macsha1, idfa, idfamd5, idfasha1, ort)
  }

  def genGPS: Gen[(Double, Double)] = for {
    lat <- Gen.choose(-90.0d, 90.0d)
    lon <- Gen.choose(-180.0d, 180.0d)
  } yield {
    (lat, lon)
  }

  def genGeoObj: Gen[GeoObj] = for {
    (lat, lon) <- genGPS
    country <- Gen.const("China")
    prov <- Gen.oneOf("Shanghai", "Hebei", "Zhejiang", "GuangDong")
    city <- Gen.oneOf("Shanghai", "Beijing", "Hangzhou", "Guangzhou")
    street <- Gen.const("")
    _type <- Gen.choose(0, 2)
    accu <- Gen.const(1)
  } yield {
    new GeoObj(lat, lon, country, prov, city, street, _type, accu)
  }

  def genImpObj: Gen[ImpObj] = for {
    id <- Gen.choose(1, 5).map(_.toString)
    tagid <- genTagId
    banner <- genBannerObj
    video <- genVideoObj
    ext <- genImpExtObj
  } yield {
    new ImpObj(id, tagid, banner, video, ext)
  }

  def genPublisherObj: Gen[PublisherObj] = for {
    (id, name, cat, domain) <- Gen.oneOf(Seq((224L, "", Seq("IAB-14"), "www..com"), (225L, "FlightManager", Seq("IAB-21"), "www.flightmanager.com")))
  } yield {
    new PublisherObj(id, name, cat, domain)
  }

  def genBidRequestObj: Gen[BidRequestObj] = for {
    id <- Gen.uuid.map(_.toString)
    ts <- Gen.const(System.currentTimeMillis())
    imp <- genImpObj
    publisher <- genPublisherObj
    app <- genAppObj
    device <- genDeviceObj
    user <- genUserObj
    tmax <- Gen.const(100)
    bcat <- Gen.someOf(Seq("IAB_5", "IAB_7", "IAB_12", "IAB_19"))
    tenantid <- Gen.choose(1L, 5L)
    ua <- genUA
    ip <- genIP
    (lat, lon) <- genGPS
  } yield {
    new BidRequestObj(id, ts, imp, publisher, app, device, user, tmax, bcat, tenantid, ua, ip, lat, lon)
  }

  def genPublisherId: Gen[Long] = Gen.choose(222, 226)
  def genChannelId: Gen[Long] = Gen.choose(454, 459)
  def genTagId: Gen[String] = Gen.choose(100, 200).map(n => s"tagid_$n")

  def genImpBeaconObj: Gen[ImpBeaconObj] = for {
    ts <- Gen.const(System.currentTimeMillis())
    bidid <- Gen.uuid.map(_.toString)
    tagid <- genTagId
    publisherid <- genPublisherId
    channelid <- genChannelId
    userid <- genUserId
    nonce <- Gen.uuid.map(_.toString)
    ip <- genIP
    tenantid <- Gen.choose(1L, 5L)
    ua <- genUA
    (lat, lon) <- genGPS
  } yield {
    new ImpBeaconObj(ts, bidid, tagid, publisherid, channelid, userid, nonce, ip, tenantid, ua, lat, lon)
  }

  def genClkPos: Gen[String] = for {
    x <- Gen.choose(1, 100)
    y <- Gen.choose(1, 250)
  } yield {
    s"$x,$y"
  }

  def genClickBeaconObj: Gen[ClickBeaconObj] = for {
    ts <- Gen.const(System.currentTimeMillis())
    clkpos <- genClkPos
    bidid <- Gen.uuid.map(_.toString)
    tagid <- genTagId
    publisherid <- genPublisherId
    channelid <- genChannelId
    userid <- genUserId
    nonce <- Gen.uuid.map(_.toString)
    ip <- genIP
    tenantid <- Gen.choose(1L, 5L)
    ua <- genUA
    (lat, lon) <- genGPS
  } yield {
    new ClickBeaconObj(ts, clkpos, bidid, tagid, publisherid, channelid, userid, nonce, ip, tenantid, ua, lat, lon)
  }
}
