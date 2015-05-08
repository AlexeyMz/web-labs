package ru.alexeymz.web.model;

import javax.persistence.*;

@Entity
@Table(name = "OrderEntries")
public class OrderEntry {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;
    private long cardId;
    private int quantity;

    public OrderEntry() {}

    public OrderEntry(Order order, long cardId, int quantity) {
        this.order = order;
        this.cardId = cardId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
