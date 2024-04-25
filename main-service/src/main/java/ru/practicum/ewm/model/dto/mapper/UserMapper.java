package ru.practicum.ewm.model.dto.mapper;

import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.dto.UserDto;
import ru.practicum.ewm.model.dto.UserShortDto;
import ru.practicum.ewm.model.request.NewUserRequest;

public class UserMapper {

    public static User toUser(NewUserRequest newUserRequest) {

        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {

        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
