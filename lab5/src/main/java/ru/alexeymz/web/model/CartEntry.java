package ru.alexeymz.web.model;

public final class CartEntry {
    Card card;
    int quantity;

    public CartEntry(Card card, int quantity) {
        this.card = card;
        this.quantity = quantity;
    }

    public Card getCard() {
        return card;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
