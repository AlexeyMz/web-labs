package ru.alexeymz.web.model;

public final class DeliveryPoint {
    private String key;
    private String coordinates;
    private String address;

    public DeliveryPoint(String key, String coordinates, String address) {
        this.key = key;
        this.coordinates = coordinates;
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getAddress() {
        return address;
    }
}
