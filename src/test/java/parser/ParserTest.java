package parser;

import de.tudarmstadt.ukp.wikipedia.api.WikiConstants;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParser() {
        String title = "Wikipedia API";

        String LF = "\n";
        String text = "India collided with Asia <ref>Hello world</ref> creating the Himalayas; Arabia collided with Eurasia, closing the [[Tethys ocean]] and creating the Zagros Mountains, around {{Ma|35}}.<ref name=Allen2008>{{cite doi|10.1016/j.palaeo.2008.04.021 }}</ref>\n";

        MediaWikiParserFactory pf = new MediaWikiParserFactory(WikiConstants.Language.english);
        MediaWikiParser parser = pf.createParser();

        ParsedPage pp = parser.parse(text);

        for (Section s : pp.getSections()){
            for (Link link : s.getLinks()) {
                assert(!link.getTarget().equals(""));
            }
        }

    }
}
