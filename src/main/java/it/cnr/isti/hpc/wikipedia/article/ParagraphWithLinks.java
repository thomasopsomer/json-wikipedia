package it.cnr.isti.hpc.wikipedia.article;

import java.util.List;

/**
 * Created by David Przybilla
 */
public class ParagraphWithLinks {

    private String paragraph;
    private List<Link> links;
    private List<Highlight> highlights;

    public ParagraphWithLinks(String paragraph, List<Link> links, List<Highlight> highlights){
        this.paragraph = paragraph;
        this.links = links;
        this.highlights = highlights;
    }

    public String getParagraph(){
        return paragraph;
    }

    public List<Link> getLinks(){
        return links;
    }

    public List<Highlight> getHighlights() {return highlights;}

}
