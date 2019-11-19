package com.datacloud.datagen.order

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object OrderProducer extends App with KafkaEnv {
  val topicName = "order"
  val producer = new OrderProducer(topicName, bootstrapServers, 1000, 10)
  producer.run()
}

case class SubOrderDetail(userId: Long, orderId: Long, subOrderId: Long,
                          siteId: Long, siteName: String, cityId: Long, cityName: String,
                          warehouseId: Long, merchandiseId: Long, price: Long, quantity: Long,
                          orderStatus: Int, isNewOrder: Int, timestamp: Long)

class OrderProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[SubOrderDetail](topicName, bootstrapServers, interval, loop) {

  override def genData: Gen[SubOrderDetail] = for {
    userId <- Gen.choose(200000L, 300000L)
    orderId <- Gen.choose(2900000000000L, 3000000000000L)
    subOrderId <- Gen.choose(1, 10).map(_ + orderId * 10)
    (siteId, siteName) <- Gen.oneOf((10219, "site_aaa"), (10220, "site_bbb"), (10220, "site_ccc"))
    (cityId, cityName) <- Gen.oneOf((101, "北京市"), (102, "上海市"), (103, "广州市"))
    warehouseId <- Gen.choose(600L, 700L)
    merchandiseId <- Gen.choose(180000L, 190000L)
    price <- Gen.choose(10L, 10000L)
    quantity <- Gen.choose(1L, 100L)
    orderStatus <- Gen.choose(0, 3)
    isNewOrder <- Gen.choose(0, 1)
    timestamp <- Gen.const(System.currentTimeMillis())
  } yield {
    SubOrderDetail(userId, orderId, subOrderId, siteId, siteName, cityId, cityName,
      warehouseId, merchandiseId, price, quantity, orderStatus, isNewOrder, timestamp)
  }

  override def getKey(t: SubOrderDetail): String = t.subOrderId.toString
}