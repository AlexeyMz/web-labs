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
                    "Упрощённая версия храпмастера 9000, но с возможностью апгрейда.\r\n" +
                    "Храпмастер 6000 с системой Total Grunt, двери из закалённого стекла. " +
                    "Устройство обладает линейным компрессором, отличаются низким уровнем шума " +
                    "и низким потреблением электроэнергии.",
                    Arrays.asList(20011L, 20012L, 20013L),
                    Arrays.asList(
                        new Feedback(30011L, "Всем доволен, рекомендую!", "dogbert", 10),
                        new Feedback(30012L, "Вместо прибора в коробке была рысь >:[", "rendall", 2),
                        new Feedback(30013L, "Достоинства: Тихий, в целом стало чище.\r\n" +
                            "Недостатки: При включении с пульта переключаются каналы спутникого телевидения.",
                            "Эдуард", 8),
                        new Feedback(30014L, "I have edited my review since becoming aware that the 6000 doesn't " +
                            "support hardware encryption my company requires either - which basically means " +
                            "there's no benefit to choosing the 6000 line over the 9000, except for the reliability " +
                            "grade that the 9000 line gets.", "Quentin Gerlach", 6),
                        new Feedback(30015L, "初めての一眼レフカメラです。この価格でこの性能ならおススメですね。",
                            "カスタマー", 4)
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
