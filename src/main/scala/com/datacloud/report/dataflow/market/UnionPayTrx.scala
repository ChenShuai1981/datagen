package com.datacloud.report.dataflow.market

import com.datacloud.report.dataflow.DataWithTime

case class UnionPayTrx(
                        // 交易账号
                        trxAccount: String,

                        // 交易金额
                        trxAmount: BigDecimal,

                        // 交易描述
                        trxDescription: String,

                        // 交易类型
                        trxType: String,

                        // 交易时间
                        trxTime: Long) extends DataWithTime {
  override def getId: String = s"${trxAccount}_${trxTime}"

  override def getTime: Long = trxTime
}
