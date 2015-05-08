package ru.alexeymz.web.data;

import ru.alexeymz.web.model.DeliveryPoint;

import java.util.List;
import java.util.Optional;

public interface DeliveryPointRepository {
    public static final String ATTRIBUTE = "DELIVERY_POINT_REPOSITORY";

    DeliveryPoint findByKey(String key);
    List<DeliveryPoint> findAll();
}
