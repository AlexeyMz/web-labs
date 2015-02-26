package ru.alexeymz.web.model;

import java.util.List;

public class SaleItem {
    private long id;
    private String name;
    private String description;
    private List<Long> imageIds;
    private List<Feedback> reviews;

    public SaleItem(
            long id,
            String name,
            String description,
            List<Long> imageIds,
            List<Feedback> reviews)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageIds = imageIds;
        this.reviews = reviews;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getImageIds() {
        return imageIds;
    }

    public String getDescription() {
        return description;
    }

    public List<Feedback> getReviews() {
        return reviews;
    }
}
