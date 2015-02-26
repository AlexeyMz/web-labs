package ru.alexeymz.web.data;

import ru.alexeymz.web.model.SaleItem;

public interface SaleItemRepository {
    SaleItem findItem(long id);
}
