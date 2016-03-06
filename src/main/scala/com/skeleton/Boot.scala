package com.skeleton

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Boot extends App {

  val conf = ConfigFactory.load()

  val sparkConf = new SparkConf().setAppName("Skeleton")
                                 .setMaster(s"local[${conf.getString("spark.cores")}]")

  implicit val sc = new SparkContext(sparkConf)

  implicit val sqlContext: SQLContext = new org.apache.spark.sql.SQLContext(sc)

  sc.stop()

}
