package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.dto.mapper.UserMapper;
import ru.practicum.ewm.model.request.NewUserRequest;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.utility.CommonPageRequest;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUser(NewUserRequest newUserRequest) {
        log.debug("RUN addCategory");

        return userRepository.save(UserMapper.toUser(newUserRequest));
    }

    @Override
    public List<User> getAllUsers(List<Long> ids, Integer from, Integer size) {
        log.debug("RUN getAllUsers");
        CommonPageRequest pageable = new CommonPageRequest(from, size);
        if (ids == null) {

            return userRepository.findAll(pageable).getContent();
        } else {

            return userRepository.findAllById(ids);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("RUN deleteUser");
        getUser(userId);
        userRepository.deleteById(userId);
    }

    private void getUser(Long userId) {
        log.debug("RUN getUser");
        Optional<User> userO = userRepository.findById(userId);
        if (userO.isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}
