package ru.alexeymz.web.model;

public class Feedback {
    private long id;
    private String text;
    private String authorName;
    /**
     * Rate in range from 0 (worst) to 10 (best)
     */
    private int rate;

    public Feedback(long id, String text, String authorName, int rate) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getRate() {
        return rate;
    }
}
