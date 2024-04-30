package ru.practicum.ewm.service;

import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.request.NewUserRequest;

import java.util.List;

public interface UserService {

    User addUserAdmin(NewUserRequest newUserRequest);

    List<User> getAllUsersAdmin(List<Long> ids, Integer from, Integer size);

    void deleteUserAdmin(Long userId);
}
