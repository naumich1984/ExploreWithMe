package ru.practicum.ewm.controller._admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.UserDto;
import ru.practicum.ewm.model.mapper.UserMapper;
import ru.practicum.ewm.model.request.NewUserRequest;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserControllerAdmin {

    private final UserService userService;

    @PostMapping("/admin/users")
    public ResponseEntity<UserDto> addUserAdmin(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("POST /admin/users");
        log.debug(" | name: {}", newUserRequest.getName());
        log.debug(" | email: {}", newUserRequest.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toUserDto(userService.addUserAdmin(newUserRequest)));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAllUsersAdmin(@RequestParam(required = false) List<Long> ids,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.debug("GET /admin/users");
        log.debug(" | ids: {}", ids);
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersAdmin(ids, from, size)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @NotNull Long userId) {
        log.debug("DELETE /admin/categories/{catId}");
        log.debug(" | userId: {}", userId);

        userService.deleteUserAdmin(userId);
    }
}
