package it.cnr.isti.hpc.wikipedia.spark

import java.io.FileWriter

import it.cnr.isti.hpc.wikipedia.reader.JsonpediaReader
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD;

/*
* @author David Przybilla <david.przybilla@idioplatform.com>
* */

class WikipediapediaRDD(pathToWiki:String, lang:String, sc:SparkContext){

  def getXMLArticles(): RDD[String] = {
    val conf = new Configuration();
    conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
    conf.set(XmlInputFormat.END_TAG_KEY, "</page>");
    conf.set(XmlInputFormat.LANG, lang);
    val language = lang

    val xmlArticles = sc.newAPIHadoopFile(pathToWiki, classOf[XmlInputFormat], classOf[LongWritable],
      classOf[Text], conf)

    xmlArticles.map(_._2.toString)
  }
}