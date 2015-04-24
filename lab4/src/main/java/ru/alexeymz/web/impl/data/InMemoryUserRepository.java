package ru.alexeymz.web.impl.data;

import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersByUsername = new HashMap<>();

    public InMemoryUserRepository() {
        User user = new User("alexey");
        user.setFirstName("Alexey");
        usersByUsername.put(user.getUsername(), user);
    }

    @Override
    public User findByUsername(String username) {
        return usersByUsername.get(username);
    }
}
