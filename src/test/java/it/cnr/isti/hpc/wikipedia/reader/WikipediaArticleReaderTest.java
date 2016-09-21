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
package it.cnr.isti.hpc.wikipedia.reader;

import static org.junit.Assert.assertTrue;

import it.cnr.isti.hpc.io.IOUtils;
import it.cnr.isti.hpc.wikipedia.article.Article;
import it.cnr.isti.hpc.wikipedia.article.Language;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * WikipediaArticleReaderTest.java
 *
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it
 * created on 18/nov/2011
 */
public class WikipediaArticleReaderTest {


	@Test
	public void testDisambiguation() throws UnsupportedEncodingException, FileNotFoundException, IOException, SAXException {
		URL u = this.getClass().getResource("/es/xml-dump/disambiguation.xml");
		WikipediaArticleReader wap = new WikipediaArticleReader(u.getFile(),"/tmp/disambiguation.json.gz", Language.ES);
		wap.start();
		String json = IOUtils.getFileAsUTF8String("/tmp/disambiguation.json.gz");
		Article a = Article.fromJson(json);
		assert(a.getType().equals(Article.Type.DISAMBIGUATION));

		URL url = this.getClass().getResource("/en/disambiguation.xml");
		WikipediaArticleReader reader = new WikipediaArticleReader(url.getFile(),"/tmp/en-disambiguation.json.gz", Language.EN);
		reader.start();

		Map<String, Article> articles = new HashMap<String, Article>();
		String[] lines = IOUtils.getFileAsUTF8String("/tmp/en-disambiguation.json.gz").split("\n");

		for(String l: lines) {
			Article article = Article.fromJson(l);
			articles.put(article.getTitle(), article);
		}


		assert(articles.get("Listed building").getType().equals(Article.Type.ARTICLE));
		assert(articles.get("Athens").getType().equals(Article.Type.ARTICLE));
		assert(articles.get("Test dab").getType().equals(Article.Type.DISAMBIGUATION));
		assert(articles.get("Test hndis").getType().equals(Article.Type.DISAMBIGUATION));
		assert(articles.get("Test disambiguation in title").getType().equals(Article.Type.ARTICLE));
		assert(articles.get("Test (disambiguation)").getType().equals(Article.Type.DISAMBIGUATION));


	}

	@Test
	public void testParsing() throws UnsupportedEncodingException, FileNotFoundException, IOException, SAXException {
		URL u = this.getClass().getResource("/en/mercedes.xml");
		WikipediaArticleReader wap = new WikipediaArticleReader(u.getFile(),"/tmp/mercedes.json.gz", Language.EN);
		wap.start();
		String json = IOUtils.getFileAsUTF8String("/tmp/mercedes.json.gz");
		Article a = Article.fromJson(json);
		assertTrue(a.getCleanText().startsWith("Mercedes-Benz"));
	}

}
