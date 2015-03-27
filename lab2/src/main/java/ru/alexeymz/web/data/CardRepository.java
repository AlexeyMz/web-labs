package ru.alexeymz.web.data;

import ru.alexeymz.web.model.Card;

import java.util.List;

public interface CardRepository {
    Card findById(long id);
    List<Card> findAllBySet(String set);
}
