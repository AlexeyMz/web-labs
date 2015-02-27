package ru.alexeymz.web.data;

import ru.alexeymz.web.model.Feedback;
import ru.alexeymz.web.model.SaleItem;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class RepositoryFactory {
    private static class Holder {
        static final RepositoryFactory INSTANCE = new RepositoryFactory();
    }

    private SaleItemRepository saleItemRepository = new SaleItemRepository() {
        @Override
        public SaleItem findItem(long id) {
            if (id == 1001) {
                return new SaleItem(id, "Храпмастер 6000",
                    "Упрощённая версия храпмастера 9000, но с возможностью апгрейда.",
                    Arrays.asList(20011L, 20012L),
                    Arrays.asList(
                        new Feedback(30011L, "Всем доволен, рекомендую!", "dogbert", 10),
                        new Feedback(30012L, "Вместо прибора в коробке была рысь >:[", "rendall", 1)
                    ));
            } else {
                return null;
            }
        }
    };

    private RepositoryFactory() {}

    public static RepositoryFactory instance() {
        return Holder.INSTANCE;
    }

    public SaleItemRepository getSaleItemRepository() {
        return saleItemRepository;
    }
}
