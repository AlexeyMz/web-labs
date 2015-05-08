package ru.alexeymz.web.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "Orders")
public final class Order {
    @Id
    @GeneratedValue
    private long id;

    private String username;
    private Calendar purchaseDate;
    private String deliveryPoint;
    private String deliveryAddress;
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<OrderEntry> entries;

    public Order() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Calendar getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Calendar purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Set<OrderEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<OrderEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", deliveryPoint='" + deliveryPoint + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", total=" + total +
                ", entries=" + entries +
                '}';
    }
}
