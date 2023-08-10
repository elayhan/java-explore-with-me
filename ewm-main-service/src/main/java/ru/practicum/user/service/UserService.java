package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getUsers(Set<Long> idSet, int from, int size);

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);
}
