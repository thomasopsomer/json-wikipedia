package it.cnr.isti.hpc.wikipedia.article;

/**
 * Created by thomasopsomer
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

    public String getText() {return text;}

    public String getType() {return type;}

    public int getStart() {return start;}

    public int getEnd() {return end;}

    @Override
    public String toString() {
        return "Highligh [text=" + getText() + ", type=" + getType() + ", start=" + getStart() + ", end=" + getEnd() + "]";
    }


}
