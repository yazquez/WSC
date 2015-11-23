package adages;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adage")
public class Adage {
    private String words;
    private int wordCount;

    public Adage() {
    }

    @Override
    public String toString() {
        return words + " -- " + wordCount + " words";
    }

    public void setWords(String words) {
        this.words = words;
        this.wordCount = words.trim().split("\\s+").length;
    }

    public String getWords() {
        return this.words;
    }

    public void setWordCount(int wordCount) {
    }

    public int getWordCount() {
        return this.wordCount;
    }
}