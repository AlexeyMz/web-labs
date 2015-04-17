package ru.alexeymz.web.data;

import ru.alexeymz.web.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card findById(long id);
    List<Card> findAllBySet(Optional<String> set);
}
