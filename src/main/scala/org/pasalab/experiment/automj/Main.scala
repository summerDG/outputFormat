package org.pasalab.experiment.automj

import java.util

import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.pasalab.experiment.{GenerateSheet, OutputExcelXlsx, OutputSheetXlsx}

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("spark://wuxiaoqi:7077").appName("test-output").getOrCreate()
    val sc = spark.sparkContext
    val ps = Seq[Row] (
      Row.fromTuple(("ed", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)),
      Row.fromTuple(("pp", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)))
    val schema = StructType(Seq(
      StructField("query", StringType),
      StructField("minExeTimeMs", DoubleType),
      StructField("maxExeTimeMs", DoubleType),
      StructField("avgExeTimeMs", DoubleType),
      StructField("exeStdDev", DoubleType),
      StructField("minOptTimeMs", DoubleType),
      StructField("maxOptTimeMs", DoubleType),
      StructField("avgOptTimeMs", DoubleType),
      StructField("optStdDev", DoubleType)
    ))
    val df = spark.createDataFrame(sc.parallelize(ps), schema)
    val generateSheet = new GenerateSheet[Performance] {
      override def generate(file: String): util.List[OutputSheetXlsx[Performance]] = {
        val list = new util.ArrayList[OutputSheetXlsx[Performance]]()
        for (i<- 0 to names.size() - 1) {
          val name = names.get(i)
          list.add(new PerformanceSheet(file, name))
        }
        list
      }
    }
    val list = new util.ArrayList[Performance]()
    df.collect().foreach(r => list.add(PerformanceFactory(10, schema, r)))
    generateSheet.appendSheet("x", list)
    val output = new OutputExcelXlsx[Performance]("test.xls", generateSheet)
    output.output()
  }
}
