package com.datacloud.datagen.userinfo

import com.datacloud.datagen.{JsonDataProducer, KafkaEnv}
import org.scalacheck.Gen

object UserInfoProducer extends App with KafkaEnv {
  val topicName = envPrefix + "user_info"
  val producer = new UserInfoProducer(topicName, bootstrapServers, 100, 9)
  producer.run()
}

class UserInfoProducer(topicName: String, bootstrapServers: String, interval: Long, loop: Int)
  extends JsonDataProducer[UserInfo](topicName, bootstrapServers, interval, loop) {

  override def genData = {
    for {
      id <- Gen.identifier
      name <- Gen.identifier
      sex <- Gen.oneOf("male", "female")
      city <- Gen.oneOf("New York", "London", "Paris")
      occupation <- Gen.oneOf("lawyer", "teacher", "worker")
      tel <- Gen.choose(923218321, 925218321).map(k => s"$k")
      fixPhoneNum <- Gen.choose(123218321, 125218321).map(k => s"$k")
      bankName <- Gen.const("the bank name")
      address <- Gen.const("the address")
      marriage <- Gen.oneOf("married", "single")
      childNum <- Gen.choose(0, 3).map(k => s"$k")
    } yield {
      UserInfo(id, name, sex, city, occupation, tel, fixPhoneNum, bankName, address, marriage, childNum)
    }
  }

  override def getKey(t: UserInfo): String = t.id
}
