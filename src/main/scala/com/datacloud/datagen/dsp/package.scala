package com.datacloud.datagen

import com.datacloud.dsp.avro.bbtree._
import org.scalacheck.Gen
import scala.collection.JavaConversions._

package object dsp {

//  case class BBTreeBannerObj(w: Option[Int],
//                             h: Option[Int],
//                             pos: Option[Int])
//
//  case class BBTreeVideoObj(mimes: Seq[String],
//                            w: Int,
//                            h: Int,
//                            pos: Option[Int],
//                            mindur: Option[Int],
//                            maxdur: Option[Int],
//                            minbitrate: Option[Int],
//                            maxbitrate: Option[Int])
//
//  case class BBTreeImpExtObj(title: Option[String],
//                             keywords: Option[String],
//                             cat: Option[String])
//
//  case class BBTreeAppObj(id: String,
//                          ver: Option[String],
//                          bundle: Option[String])
//
//  case class BBTreeDeviceObj(ip: String,
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
//  case class BBTreeGeoObj(lat: Option[Double],
//                          lon: Option[Double],
//                          country: Option[String],
//                          prov: Option[String],
//                          city: Option[String],
//                          street: Option[String],
//                          `type`: Option[Int],
//                          accu: Option[Int])
//
//  case class BBTreeImpObj(id: String,
//                          tagid: String,
//                          banner: Option[BBTreeBannerObj],
//                          video: Option[BBTreeVideoObj],
//                          ext: Option[BBTreeImpExtObj])
//
//  case class BBTreeUserObj(id: String,
//                           gender: Option[String],
//                           yob: Option[Int],
//                           keywords: Option[String],
//                           ext: Option[Map[String, String]])
//
//  case class BBTreeBidRequestObj(id: String,
//                                 imp: Seq[BBTreeImpObj],
//                                 app: BBTreeAppObj,
//                                 device: BBTreeDeviceObj,
//                                 user: BBTreeUserObj,
//                                 tmax: Option[Int],
//                                 bcat: Option[Seq[String]])
//
//  case class BBTreeBidResponseExtObj(title: Option[String],
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
//  case class BBTreeBidResponseObj(id: String,
//                                  bidid: String,
//                                  adm: Option[String],
//                                  cat: Option[String],
//                                  ext: BBTreeBidResponseExtObj)

  def genUserId: Gen[String] = Gen.choose(10000, 99999).map(n => s"userid_$n")

  def genBBTreeUserObj: Gen[BBTreeUserObj] = for {
    id <- Gen.uuid.map(_.toString)
    gender <- Gen.oneOf(Seq("M", "F", "O"))
    yob <- Gen.choose(1970, 2000)
    keywords <- Gen.oneOf("food", "travel", "music", "sports")
    ext <- Gen.const(Map.empty[String, String])
  } yield {
    new BBTreeUserObj(id, gender, yob, keywords, ext)
  }

  def genBBTreeBannerObj: Gen[BBTreeBannerObj] = for {
    (w, h) <- Gen.oneOf(Seq((50, 100), (80, 150), (32, 48)))
    pos <- Gen.choose(0, 7)
  } yield {
    new BBTreeBannerObj(w, h, pos)
  }

  def genBBTreeVideoObj: Gen[BBTreeVideoObj] = for {
    mimes <- Gen.someOf(Seq("avi", "mkv", "mp4"))
    (w, h) <- Gen.oneOf(Seq((50, 100), (80, 150), (32, 48)))
    pos <- Gen.choose(0, 7)
    mindur <- Gen.choose(1000, 3000)
    maxdur <- Gen.choose(10000, 30000)
    minbitrate <- Gen.choose(1000, 3000)
    maxbitrate <- Gen.choose(10000, 30000)
  } yield {
    new BBTreeVideoObj(mimes, w, h, pos, mindur, maxdur, minbitrate, maxbitrate)
  }

  def genBBTreeImpExtObj: Gen[BBTreeImpExtObj] = for {
    title <- Gen.oneOf(Seq("hello world"))
    keywords <- Gen.oneOf(Seq("women", "sports", "tv", "music"))
    cat <- Gen.someOf(Gen.choose(1, 10).map(n => s"IAB_$n"), Gen.choose(11, 20).map(n => s"IAB_$n"))
  } yield {
    new BBTreeImpExtObj(title, keywords, cat)
  }

  def genBBTreeAppObj: Gen[BBTreeAppObj] = for {
    id <- genSiteId
    ver <- Gen.choose(1, 100).map(_.toString)
    bundle <- Gen.const("com.bbtree.app")
  } yield {
    new BBTreeAppObj(id, ver, bundle)
  }

  def genIP: Gen[String] = for {
      d1 <- Gen.choose(0, 255)
      d2 <- Gen.choose(0, 255)
      d3 <- Gen.choose(0, 255)
      d4 <- Gen.choose(0, 255)
    } yield {
      s"$d1.$d2.$d3.$d4"
    }

  def genBBTreeDeviceObj: Gen[BBTreeDeviceObj] = for {
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
    new BBTreeDeviceObj(
      ip, ipv6, devtype, lang, make, model, os, osv, conntype,
      didmd5, didsha1, dpidmd5, dpidsha1,
      macmd5, macsha1, idfa, idfamd5, idfasha1, ort)
  }

  def genBBTreeGeoObj: Gen[BBTreeGeoObj] = for {
    lat <- Gen.choose(-90.0d, 90.0d)
    lon <- Gen.choose(-180.0d, 180.0d)
    country <- Gen.const("China")
    prov <- Gen.oneOf("Shanghai", "Hebei", "Zhejiang", "GuangDong")
    city <- Gen.oneOf("Shanghai", "Beijing", "Hangzhou", "Guangzhou")
    street <- Gen.const("")
    _type <- Gen.choose(0, 2)
    accu <- Gen.const(1)
  } yield {
    new BBTreeGeoObj(lat, lon, country, prov, city, street, _type, accu)
  }

  def genBBTreeImpObj: Gen[BBTreeImpObj] = for {
    id <- Gen.choose(1, 5).map(_.toString)
    tagid <- genPlacementId
    banner <- genBBTreeBannerObj
    video <- genBBTreeVideoObj
    ext <- genBBTreeImpExtObj
  } yield {
    new BBTreeImpObj(id, tagid, banner, video, ext)
  }

  def genPublisherObj: Gen[BBTreePublisherObj] = for {
    (id, name, cat, domain) <- Gen.oneOf(Seq(("BBTree", "BBTree", Seq("IAB-14"), "www.bbtree.com"), ("FlightManager", "FlightManager", Seq("IAB-21"), "www.flightmanager.com")))
  } yield {
    new BBTreePublisherObj(id, name, cat, domain)
  }

  def genBBTreeBidRequestObj: Gen[BBTreeBidRequestObj] = for {
    id <- Gen.uuid.map(_.toString)
    ts <- Gen.const(System.currentTimeMillis())
    imp <- genBBTreeImpObj
    publisher <- genPublisherObj
    app <- genBBTreeAppObj
    device <- genBBTreeDeviceObj
    user <- genBBTreeUserObj
    tmax <- Gen.const(100)
    bcat <- Gen.someOf(Seq("IAB_5", "IAB_7", "IAB_12", "IAB_19"))
  } yield {
    new BBTreeBidRequestObj(id, ts, imp, publisher, app, device, user, tmax, bcat)
  }

  def genPublisherId: Gen[String] = Gen.choose(222, 223).map(n => s"publisherid_$n")
  def genSiteId: Gen[String] = Gen.choose(444, 445).map(n => s"siteid_$n")
  def genPlacementId: Gen[String] = Gen.choose(1, 5).map(n => s"placementid_$n")
  def genCreativeId: Gen[String] = Gen.choose(666, 777).map(n => s"creativeid_$n")

  def genBBTreeImpBeaconObj: Gen[BBTreeImpBeaconObj] = for {
    ts <- Gen.const(System.currentTimeMillis())
    bidid <- Gen.uuid.map(_.toString)
    placementid <- genPlacementId
    publisherid <- genPublisherId
    siteId <- genSiteId
    creativeId <- genCreativeId
    userId <- genUserId
  } yield {
    new BBTreeImpBeaconObj(ts, bidid, placementid, publisherid, siteId, creativeId, userId)
  }

  def genClkPos: Gen[String] = for {
    x <- Gen.choose(1, 100)
    y <- Gen.choose(1, 250)
  } yield {
    s"$x,$y"
  }

  def genBBTreeClickBeaconObj: Gen[BBTreeClickBeaconObj] = for {
    ts <- Gen.const(System.currentTimeMillis())
    clkpos <- genClkPos
    bidid <- Gen.uuid.map(_.toString)
    placementid <- genPlacementId
    publisherid <- genPublisherId
    siteId <- genSiteId
    creativeId <- genCreativeId
    userId <- genUserId
  } yield {
    new BBTreeClickBeaconObj(ts, clkpos, bidid, placementid, publisherid, siteId, creativeId, userId)
  }
}
