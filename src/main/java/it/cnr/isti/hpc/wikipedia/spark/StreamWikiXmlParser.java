package it.cnr.isti.hpc.wikipedia.spark;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.WikiXMLParser;
import org.xml.sax.SAXException;

import java.io.*;

/*
* @author David Przybilla <david.przybilla@idioplatform.com>
* */
public class StreamWikiXmlParser extends WikiXMLParser {

    public StreamWikiXmlParser(String xmlContent, IArticleFilter filter) throws UnsupportedEncodingException,
            IOException, SAXException, FileNotFoundException {
        super(getBufferedReaderFromString(xmlContent), filter);
    }

    public static BufferedReader getBufferedReaderFromString(String XMLInput) throws UnsupportedEncodingException,
            FileNotFoundException, IOException {
        BufferedReader br = null;
        InputStream is = new ByteArrayInputStream(XMLInput.getBytes());
        br = new BufferedReader(new InputStreamReader(is));
        return br;
    }
}
