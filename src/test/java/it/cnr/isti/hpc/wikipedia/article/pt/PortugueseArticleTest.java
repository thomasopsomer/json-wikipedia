package it.cnr.isti.hpc.wikipedia.article.pt;

import it.cnr.isti.hpc.io.IOUtils;
import it.cnr.isti.hpc.wikipedia.article.*;
import it.cnr.isti.hpc.wikipedia.parser.ArticleParser;
import org.apache.commons.math3.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 * Created by dav009 on 05/01/2016.
 */
public class PortugueseArticleTest extends ArticleTest {
	private static Article a = new Article();
	private static ArticleParser articleParser = new ArticleParser(Language.PT);

	@BeforeClass
	public static void loadArticle() throws IOException {
		String text = readFileAsString("/pt/xml-dump/article.txt");
		articleParser.parse(a, text);
	}

	@Test
	public void sections() throws IOException {
		assertThat(a.getSections(), hasItems("História", "Personagens", "Staff",
				"Seiyuu/Dubladores", "Episódios", "Música", "Curiosidades"));
	}

	@Test
	public void categories() throws IOException {
		List<String> categories = new ArrayList<String>();
		for(Link l: a.getCategories())
			categories.add(l.getAnchor());

		assertThat(categories, hasItems("Categoria:Série Gundam"));
	}

	@Test
	public void links() throws IOException {
		List<String> anchors = new ArrayList<String>();
		List<String> uris = new ArrayList<String>();

		for (ParagraphWithLinks p : a.getParagraphsWithLinks()) {
			for(Link l:p.getLinks()){
				anchors.add(l.getAnchor());
			}
		}

		for (ParagraphWithLinks p : a.getParagraphsWithLinks()) {
			for(Link l:p.getLinks()){
				uris.add(l.getId());
			}
		}

		assertThat(anchors, hasItems("L'Arc~en~Ciel", "The Brilliant Green", "Anime Grand Prix", "Hajime Yatate"));
		assertThat(uris, hasItems("Sunrise", "L'Arc~en~Ciel", "The_Brilliant_Green", "Hajime_Yatate", "Mamoru_Miyano" ));
		assertFalse(anchors.contains("japaneselang"));
		assertFalse(anchors.contains(":japaneselang"));
		assertFalse(anchors.contains("ja:japaneselang"));
	}


	@Test
	public void testAnnotationsWithOtherNamespaces() throws IOException {
		Article a = new Article();
		String mediawiki = IOUtils.getFileAsUTF8String("./src/test/resources/pt/xml-dump/article_with_weird_annotations");
		articleParser.parse(a, mediawiki);
		Pair<List<String>, List<String>> anchorsAndUris = getAnchorsAndUris(a);
		List<String> uris = anchorsAndUris.getSecond();
		assert(uris.size()==0);
	}
}
