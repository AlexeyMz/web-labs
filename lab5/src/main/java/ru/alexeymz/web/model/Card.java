package ru.alexeymz.web.model;

import java.math.BigDecimal;
import java.util.List;

public final class Card {
    private long id;
    private String set;
    private int numberInSet;
    private String name;
    private String manaCost;
    private String cardText;
    private String flavorText;
    private List<String> imageIds;
    private List<Feedback> reviews;
    private BigDecimal price;

    public Card(
            long id,
            String set,
            int numberInSet,
            String name,
            String manaCost,
            String cardText,
            String flavorText,
            List<String> imageIds,
            List<Feedback> reviews,
            BigDecimal price)
    {
        this.id = id;
        this.set = set;
        this.numberInSet = numberInSet;
        this.name = name;
        this.manaCost = manaCost;
        this.cardText = cardText;
        this.flavorText = flavorText;
        this.imageIds = imageIds;
        this.reviews = reviews;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getSet() {
        return set;
    }

    public int getNumberInSet() {
        return numberInSet;
    }

    public String getName() {
        return name;
    }

    public String getManaCost() {
        return manaCost;
    }

    public String getCardText() {
        return cardText;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public List<String> getImageIds() {
        return imageIds;
    }
    public List<Feedback> getReviews() {
        return reviews;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
