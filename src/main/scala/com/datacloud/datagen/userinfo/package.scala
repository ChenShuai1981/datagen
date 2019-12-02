package com.datacloud.datagen

package object userinfo {

  case class UserInfo (
                        id: String,
                        name: String,
                        sex: String,
                        city: String,
                        occupation: String,
                        tel: String,
                        fixPhoneNum: String,
                        bankName: String,
                        address: String,
                        marriage: String,
                        childNum: String
                      )

}
