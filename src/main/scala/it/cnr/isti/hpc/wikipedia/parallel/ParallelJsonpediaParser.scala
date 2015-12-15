package it.cnr.isti.hpc.wikipedia.parallel

import java.io.{File, FileWriter}
import java.util.concurrent.atomic.AtomicInteger

import it.cnr.isti.hpc.wikipedia.reader.WikipediaArticleReader
import org.apache.commons.io.FileUtils

object ParallelJsonpediaParser{

  def getHeader(pathToSingleWikiDump: String): String ={
    // First line contains the header:
    // <mediawiki xmlns="http://www.mediawiki.org/xml/export-0.10/"....
    scala.io.Source.fromFile(pathToSingleWikiDump).getLines().next()
  }

  def getNamespaces(pathToSingleWikiDump: String): String ={
    val firstLines = scala.io.Source.fromFile(pathToSingleWikiDump).getLines().slice(0, 90000).toList
    println(firstLines(10))
    val start = firstLines.indexWhere(l => l.trim.equals("<siteinfo>"))
    val end = firstLines.indexWhere(l => l.trim.equals("</siteinfo>")) + 1
    println("start:" + start  )
    println("end: "+ end)
    firstLines.slice(start, end).mkString(" ")
  }

  /*
  * Given a list of files it appends the <mediawiki> tag at the beginning and </mediawiki> at the end
  * of each file.
  * Note: Each of the given files willl be overwritten.
  * @param files: List of Files
  * */
  def appendHeaderFooter(files:Seq[File], header: String, namespaces: String): Unit ={
    val footer = "</mediawiki>"

    println("Adding headers, footers to files...")
    files.par.map{ f =>
      println("processing.." + f.getName())
      val content = scala.io.Source.fromFile(f,  "utf-8").getLines().toList
      val fw = new FileWriter(f, false)
      fw.write(header +"\n")
      fw.write(namespaces + "\n")
      content.foreach(line => fw.write(line + "\n"))
      fw.write(footer)
      fw.close()
    }
  }

  /*
  * Takes a list of files containing a xml wikipedia dump
  * and in parallel calls WikipediaArticleReader
  * and generates a json wikipedia dump per input file
  *
  * @pathToFiles: Path to folder containing wikipedia xml files
  * @ouput: Path to folder where the resulting jsonWiki
  * */
  def exportToJsonpedia(pathToFiles:String, output:String, lang:String, pathToSingleWikiDump: String): Unit ={

    val header = getHeader(pathToSingleWikiDump)
    val namespaces = getNamespaces(pathToSingleWikiDump)


    val wikiXmlFiles = new File(pathToFiles).listFiles().filter(_.getName().startsWith("part"))
    appendHeaderFooter(wikiXmlFiles, header, namespaces)

    val total = wikiXmlFiles.length
    val done = new AtomicInteger()

    wikiXmlFiles.par.map{ file =>
      val wap: WikipediaArticleReader = new WikipediaArticleReader(file.getAbsolutePath, output + "/" + file.getName + ".json", lang)
      try {
        println("%s%% done".format(done.incrementAndGet().floatValue()/total * 100))
        wap.start
        //Deleting the input xml file to assure enough space
        //FileUtils.deleteQuietly(file)
      }
      catch {
        case e: Exception => {
          println("Error parsing the mediawiki {}", e.toString)
          System.exit(-1)
        }
      }
    }
  }

}
