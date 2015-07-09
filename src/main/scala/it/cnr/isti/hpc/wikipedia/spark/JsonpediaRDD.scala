package it.cnr.isti.hpc.wikipedia.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD;

/*
* @author David Przybilla <david.przybilla@idioplatform.com>
* */

class JsonpediaRDD(pathToWiki:String, lang:String, sc:SparkContext){

  def parse():RDD[String]={

    val conf = new Configuration();
    conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
    conf.set(XmlInputFormat.END_TAG_KEY, "</page>");
    conf.set(XmlInputFormat.LANG, lang);

    val jsonArticles = sc.newAPIHadoopFile(pathToWiki, classOf[XmlInputFormat], classOf[LongWritable],
      classOf[Text], conf)
    jsonArticles.map(_._2.toString)
  }

}
