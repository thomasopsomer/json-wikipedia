package it.cnr.isti.hpc.wikipedia.article;

import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import it.cnr.isti.hpc.wikipedia.parser.ArticleParser;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

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

	@Test
	public void testExtractingLinksOtherLang(){

		String mediawiki = "* La Douceur de croire, pièce en trois actes, Théâtre Français, 8 July 1899\n" +
				"\n" +
				"[[:France]]In collaboration with [[:fr:André Delavigne]] " +
				"* Blakson père [[::::::Potato|Pommes]] et fils,[[fr:Something]] comédie en quatre actes, Théâtre de l'Odéon\n" +
				"* Les petites  marmites, comédie en trois actes, Théâtre du Gymnase\n" +
				"[[cite:Gundam]] * Voilà Monsieur !, comédie en un acte, Théâtre du Gymnase";

		ArticleParser parser = new ArticleParser(Language.EN);
		Article a = new Article();
		parser.parse(a, mediawiki);

		Pair<List<String>, List<String>> anchorsAndUris = getAnchorsAndUris(a);
		List<String> uris = anchorsAndUris.getSecond();

		assertFalse(uris.contains("André_Delavigne"));
		assertFalse(uris.contains("Something"));
//		assertThat(uris, hasItems("France", "Potato", "cite:Gundam"));
        assertThat(uris, hasItems("France", "Potato"));
        assert(uris.size()==2);

//		 Making sure Links with ":" are considered Internals
		Link andreAnnotation = getLink(a, "France");
		assertEquals(andreAnnotation.getType(), Link.type.INTERNAL);

		Link potato = getLink(a, "Potato");
		assertEquals(potato.getType(), Link.type.INTERNAL);
		assertEquals(potato.getAnchor(), "Pommes");

		testAnchorsInText(a);

	}

	/*
	* Get a list with all the annotated URIs
	* */
	private Link getLink(Article page, String uri){
		for (Link link : page.getLinks()) {
            if (uri.equals(link.getId()))
                return link;
        }
        return null;
	}
}
