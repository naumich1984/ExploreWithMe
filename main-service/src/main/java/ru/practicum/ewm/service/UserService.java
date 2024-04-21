package ru.practicum.ewm.service;

import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.dto.UserDto;
import ru.practicum.ewm.model.request.NewUserRequest;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface UserService {

    User addUser(NewUserRequest newUserRequest);

    List<User> getAllUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
