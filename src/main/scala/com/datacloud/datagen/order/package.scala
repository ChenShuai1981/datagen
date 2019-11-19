package com.datacloud.datagen

package object order {
<<<<<<< HEAD

=======
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
>>>>>>> ad393f7091de855dec8c28b578de64be41f01242
}
