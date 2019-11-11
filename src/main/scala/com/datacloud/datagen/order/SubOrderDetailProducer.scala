package com.datacloud.datagen.order

import com.datacloud.datagen.JsonDataProducer
import org.scalacheck.Gen

object SubOrderDetailProducer extends App {
  val topicName = "order"
  val bootstrapServers = "localhost:9092"

  val interval = 60L
  val loop = 1

  val producer = new SubOrderDetailProducer(topicName, bootstrapServers, interval, loop)
  producer.run()
}

class SubOrderDetailProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[SubOrderDetail](topicName, bootstrapServers, interval, loop) {

  override def genData = {

    def genSite: Gen[(Long, String)] = Gen.oneOf(Seq((1L, "site1"), (2L, "site2"), (3L, "site3")))
    def genCity: Gen[(Long, String)] = Gen.oneOf(Seq((101L, "NewYork"), (102L, "Beijing"), (103L, "Paris"), (104L, "London"), (105L, "Tokyo")))

    for {
      userId <- Gen.choose(10000L, 99999L)
      orderId <- Gen.choose(100L, 999L)
      subOrderId <- Gen.choose(1000L, 9999L)
      (siteId, siteName) <- genSite
      (cityId, cityName) <- genCity
      warehouseId <- Gen.choose(1000L, 9999L)
      merchandiseId <- Gen.choose(10000L, 99999L)
      price <- Gen.choose(1, 999)
      quantity <- Gen.choose(1, 100)
      orderStatus <- Gen.choose(1, 3)
      isNewOrder <- Gen.choose(0, 1)
      timestamp <- Gen.const(System.currentTimeMillis())
    } yield {
      SubOrderDetail(userId, orderId, subOrderId, siteId, siteName, cityId,
        cityName, warehouseId, merchandiseId, price, quantity, orderStatus, isNewOrder, timestamp)
    }
  }
}
