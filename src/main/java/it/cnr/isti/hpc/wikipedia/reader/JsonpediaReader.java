package it.cnr.isti.hpc.wikipedia.reader;

import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Created by dav009 on 09/07/15.
 */
public class JsonpediaReader {

    private String lang;
    private String XMLInput;

    public JsonpediaReader(String XMLInput, String lang){
        this.lang = lang;
        this.XMLInput = XMLInput;
    }

    public String getJson() throws IOException, SAXException {
        WikipediaArticleReader wap = new WikipediaArticleReader(XMLInput, lang);
        wap.start();
        return wap.getJson();
    }
}
