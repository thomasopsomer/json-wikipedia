package it.cnr.isti.hpc.wikipedia.article;

import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dav009 on 05/01/2016.
 */
public class ArticleTest {

	protected Pair<List<String>, List<String>> getAnchorsAndUris(Article a){

		List<String> uris = new ArrayList<String>();
		List<String> anchors = new ArrayList<String>();

		for (ParagraphWithLinks p : a.getParagraphsWithLinks()) {
			for(Link l:p.getLinks()){
				uris.add(l.getId());
				anchors.add(l.getAnchor());
			}
		}

		return new Pair<List<String>, List<String>>(anchors, uris);
	}

	/*
	* Matches the extracted anchors and spans against the text
	* they have been extracted from.
	* */
	protected void testAnchorsInText(Article article){
		for(ParagraphWithLinks p:  article.getParagraphsWithLinks()){
			for(Link link: p.getLinks()){
				String anchorInPar = p.getParagraph().substring(link.getStart(),link.getEnd() );
				assertEquals(anchorInPar, link.getAnchor());
			}
		}
	}


	protected static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				ArticleTest.class.getResourceAsStream(filePath), "UTF-8"));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
