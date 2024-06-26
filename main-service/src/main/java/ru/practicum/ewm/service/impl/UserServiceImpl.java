package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.mapper.UserMapper;
import ru.practicum.ewm.model.request.NewUserRequest;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.utility.CommonPageRequest;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUserAdmin(NewUserRequest newUserRequest) {
        log.debug("RUN addCategory");

        return userRepository.save(UserMapper.toUser(newUserRequest));
    }

    @Override
    public List<User> getAllUsersAdmin(List<Long> ids, Integer from, Integer size) {
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
    public void deleteUserAdmin(Long userId) {
        log.debug("RUN deleteUser");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
