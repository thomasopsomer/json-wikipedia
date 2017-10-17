package de.tudarmstadt.ukp.wikipedia.parser;

import de.tudarmstadt.ukp.wikipedia.api.WikiConstants;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dav009 on 23/06/2015.
 */
public class ParserTest {


    public boolean findLinkInList(Link l, Collection<Link> list){
         for(Link currentLink:list) {
             if (l.equals(currentLink)) return true;
         }
        return false;
    }

	/*
	* Matches the extracted anchors and spans against the text
	* they have been extracted from.
	* */
	private void testAnchorsInText(ParsedPage page){

		for (Section s : page.getSections()){
			for (Paragraph p: s.getParagraphs()){
				for (Link link : p.getLinks()) {
					String anchorInPar = p.getText().substring(link.getPos().getStart(), link.getPos().getEnd());
					assertEquals(anchorInPar, link.getText());
				}
			}
		}
	}

	/*
	* Get a list with all the annotated URIs
	* */
	public List<String> getUrisInParagraphs(ParsedPage page){
		List<String> uris = new ArrayList<String>();
		for (Section s : page.getSections()){
			for (Paragraph p: s.getParagraphs()){
				for (Link link : p.getLinks()) {
					uris.add(link.getTarget());
				}
			}
		}
		return uris;
	}

	/*
	* Get a list with all the annotated URIs
	* */
	public Link getLink(ParsedPage page, String uri){
		for (Section s : page.getSections()){
			for (Paragraph p: s.getParagraphs()){
				for (Link link : p.getLinks()) {
					if (uri.equals(link.getTarget()))
						return link;
				}
			}
		}
		return null;
	}

    @Test
    public void testExtractingLinksWithColons(){

		// Regular paragraphs
        String text ="'''Hayami''' Rena Hayami, [[Image:a.jgp]] images and file namespaces [[File:a.jpg]] " +
				"''[[Category: Evolution]]'' [[Category:Jackson musical family]].\n \n Kohinata Hayami, " +
				"[[H2O: Footprints in the Sand]] character. [[Cite:AAA]] [[Noriko Hayami]] (born 1959), Japanese actress.";
        MediaWikiParserFactory pf = new MediaWikiParserFactory(WikiConstants.Language.english);
        MediaWikiParser parser = pf.createParser();

        ParsedPage pp = parser.parse(text);

        List<String> uris = getUrisInParagraphs(pp);
        assertThat(uris, hasItems("H2O:_Footprints_in_the_Sand", "Noriko_Hayami"));

        Link h2OAnnotation = getLink(pp, "H2O:_Footprints_in_the_Sand");
        assertEquals(h2OAnnotation.getType(), Link.type.UNKNOWN);
        assertEquals(getLink(pp, "Cite:AAA").getType(), Link.type.UNKNOWN);

        testAnchorsInText(pp);
    }
}
