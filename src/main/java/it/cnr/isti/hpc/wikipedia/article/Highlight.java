package it.cnr.isti.hpc.wikipedia.article;

/**
 * Created by thomasopsomer on 23/10/2017.
 */
public class Highlight {


    private String text;
    private String type;
    private int start;
    private int end;

    public Highlight(String text, String type, int start, int end){
        this.text = text;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String getAnchor() {return text;}

    public String getType() {return type;}

    public int getStart() {return start;}

    public int getEnd() {return end;}


}
