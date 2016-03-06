package com.skeleton

import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.{Matchers, FlatSpec}


class BootSpec extends FlatSpec with Matchers with SharedSparkContext {

  implicit val sparkContext = sc

  "sc" should "be mocked by SharedSparkContext in this test" in {
    sc.parallelize(Seq(1)).collect().head should be(1)
  }

}
