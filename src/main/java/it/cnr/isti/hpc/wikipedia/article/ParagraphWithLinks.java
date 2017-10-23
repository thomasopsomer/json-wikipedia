package it.cnr.isti.hpc.wikipedia.article;

import org.apache.commons.collections.ListUtils;

import java.util.List;

/**
 * Created by David Przybilla
 */
public class ParagraphWithLinks {

    private String paragraph;
    private List<Link> links;
    private List<Highlight> highlighs;
//    private List<String> bolds;
//    private List<String> italics;

    public ParagraphWithLinks(String paragraph, List<Link> links, List<Highlight> highlights){
        this.paragraph = paragraph;
        this.links = links;
        this.highlighs = highlights;
//        this.bolds = bolds;
//        this.italics = italics;
    }

    public String getParagraph(){
        return paragraph;
    }

    public List<Link> getLinks(){
        return links;
    }

    public List<Highlight> getHighlights() {return highlighs;}

//    public List<String> getBolds() {return bolds;}
//
//    public List<String> getItalics() {return italics;}
//
//    public List<String> getHighlights() {return ListUtils.union(italics, bolds)}

}
