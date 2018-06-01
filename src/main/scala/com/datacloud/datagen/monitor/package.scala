package com.datacloud.datagen

package object monitor {

  case class MonitorTableResult(tenantId: Long, hitTime: Long, hitCounts: Int)

  case class MonitorStreamResult(tenantId: Long, productCode: String, eventCode: String, terminal: String, occurTime: Long)

}
