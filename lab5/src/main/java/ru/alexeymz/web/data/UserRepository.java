package ru.alexeymz.web.data;

import ru.alexeymz.web.model.User;

public interface UserRepository {
    public static final String ATTRIBUTE = "USER_REPOSITORY";

    User findByUsername(String username);
}
