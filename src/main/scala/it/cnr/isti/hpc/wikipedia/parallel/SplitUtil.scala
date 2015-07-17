package it.cnr.isti.hpc.wikipedia.parallel

import java.io.{File, FileWriter}

import it.cnr.isti.hpc.wikipedia.reader.WikipediaArticleReader

object SplitUtil{

  /*
  * Given a list of files it appends the <mediawiki> tag at the beginning and </mediawiki> at the end
  * of each file.
  * Note: Each of the given files willl be overwritten.
  * @param files: List of Files
  * */
  def appendHeaderFooter(files:Seq[File]): Unit ={
    val header = "<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.10/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.10/ http://www.mediawiki.org/xml/export-0.10.xsd\" version=\"0.10\" xml:lang=\"en\">"
    val footer = "</mediawiki>"
    println("Adding headers, footers to files...")
    files.par.map{ f =>
      println("processing.." + f.getName())
      val content = scala.io.Source.fromFile(f,  "utf-8").getLines().toList
      val fw = new FileWriter(f, false)
      fw.write(header +"\n")
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
  def exportToJsonpedia(pathToFiles:String, output:String, lang:String): Unit ={
    val wikiXmlFiles = new File(pathToFiles).listFiles().filter(_.getName().startsWith("part"))
    appendHeaderFooter(wikiXmlFiles)
    wikiXmlFiles.par.map{ file =>
      val wap: WikipediaArticleReader = new WikipediaArticleReader(file.getAbsolutePath, output + "/" + file.getName + ".json", lang)
      try {
        println("starting parsing wiki.." + file.getAbsolutePath)
        wap.start
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
