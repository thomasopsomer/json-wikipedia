/**
 *  Copyright 2011 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.wikipedia.article.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItems;

import it.cnr.isti.hpc.io.IOUtils;
import it.cnr.isti.hpc.wikipedia.article.*;
import it.cnr.isti.hpc.wikipedia.parser.ArticleParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ArticleTest.java
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 19/nov/2011
 */
public class ItalianArticleTest extends ArticleTest {
	private static Article a = new Article();
	private static ArticleParser articleParser = new ArticleParser(Language.IT);

	@BeforeClass
	public static void loadArticle() throws IOException {
		String text = readFileAsString("/it/xml-dump/article.txt");

		articleParser.parse(a, text);

	}

	@Test
	public void sections() throws IOException {
		assertTrue(a.getSections().contains("Armonium occidentale"));
		assertTrue(a.getSections().contains("Armonium indiano"));
		assertTrue(a.getSections().contains("Bibliografia"));
		assertTrue(a.getSections().contains("Collegamenti esterni"));
	}

	@Test
	public void categories() throws IOException {

		assertEquals(1, a.getCategories().size());
		assertEquals("Categoria:Aerofoni a mantice", a.getCategories().get(0)
				.getAnchor());
	}

	@Test
	public void links() throws IOException {

		List<String> anchors = new ArrayList<String>();

		for (ParagraphWithLinks p : a.getParagraphsWithLinks()) {
			for(Link l:p.getLinks()){
				anchors.add(l.getAnchor());
			}
		}

		assertThat(anchors, hasItems("strumento musicale", "Giovanni Tamburini", "temperamento equabile",
				                     "Stuart Isacoff", "Generi musicali"));
		assertFalse(anchors.contains("Categoria:Aerofoni a mantice"));
		assertFalse(anchors.contains(":Aerofoni a mantice"));
		assertFalse(anchors.contains("Aerofoni a mantice"));
		assertFalse(anchors.contains("Fisharmonija"));
		assertFalse(anchors.contains("lt:Fisharmonija"));
		assertFalse(anchors.contains(":Fisharmonija"));
		testAnchorsInText(a);
	}


	@Test
	public void table() throws IOException {
		Article articleWithTable = new Article();
		String text = readFileAsString("/it/xml-dump/table.txt");
		articleParser.parse(articleWithTable, text);
		assertEquals("Nome italiano", articleWithTable.getTables().get(0)
				.getColumn(1).get(0));
		assertEquals("15 agosto", articleWithTable.getTables().get(0)
				.getColumn(0).get(10));

	}

	@Test
	public void list() throws IOException {
		String text = readFileAsString("/it/xml-dump/list.txt");
		Article articleWithList = new Article();
		articleParser.parse(articleWithList, text);
		List<String> list = articleWithList.getLists().get(2);
		assertEquals("Antropologia culturale e Antropologia dei simboli", list.get(0));
	}

	@Test
	public void testAnnotationsWithOtherNamespaces() throws IOException {
		Article a = new Article();
		String mediawiki = IOUtils.getFileAsUTF8String("./src/test/resources/it/xml-dump/article_with_weird_annotations");
		articleParser.parse(a, mediawiki);
		Pair<List<String>, List<String>> anchorsAndUris = getAnchorsAndUris(a);
		List<String> anchors = anchorsAndUris.getFirst();
		List<String> uris = anchorsAndUris.getSecond();
		assertThat(uris, hasItems("Harry_Potter", "Sōtō", "Arashiyama", "Kyoto", "Università_degli_Studi_di_Napoli_Federico_II"));
		assert(uris.size()==7);

		assertFalse(uris.contains("File:Yukipon_SxH1.jpg"));
		assertFalse(uris.contains("File:Nunz2.JPG"));
		assertFalse(uris.contains("Nunz2.JPG"));

		testAnchorsInText(a);
	}

	@Test
	public void testAnnotationsInLists() throws IOException {
		Article a = new Article();
		String mediawiki = IOUtils.getFileAsUTF8String("./src/test/resources/it/xml-dump/YUKI");
		articleParser.parse(a, mediawiki);

		Pair<List<String>, List<String>> anchorsAndUris = getAnchorsAndUris(a);
		List<String> anchors = anchorsAndUris.getFirst();
		List<String> uris = anchorsAndUris.getSecond();

		assertThat(uris, hasItems("Honey_and_Clover", "2005", "POWERS_OF_TEN"));
		assertThat(anchors, hasItems("Honey and Clover", "2005", "POWERS OF TEN"));
		testAnchorsInText(a);
	}


}
