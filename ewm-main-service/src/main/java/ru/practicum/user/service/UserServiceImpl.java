package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static ru.practicum.util.PageableCreator.getPageable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getUsers(Set<Long> idSet, int from, int size) {
        Pageable pageable = getPageable(from, size);
        return mapper.userListToUserDtoList(repository.getUsers(idSet, pageable));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = mapper.userDtoToUser(userDto);
        return mapper.userToUserDto(repository.save(user));
    }

    @Override
    public void deleteUser(long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        repository.deleteById(userId);
    }
}
