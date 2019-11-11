package com.datacloud.datagen

package object order {
  case class SubOrderDetail(userId: Long,
                            orderId: Long,
                            subOrderId: Long,
                            siteId: Long,
                            siteName: String,
                            cityId: Long,
                            cityName: String,
                            warehouseId: Long,
                            merchandiseId: Long,
                            price: Long,
                            quantity: Long,
                            orderStatus: Int,
                            isNewOrder: Int,
                            timestamp: Long)
}
