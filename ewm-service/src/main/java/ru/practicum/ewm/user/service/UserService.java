package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.model.dto.UserInputDto;
import ru.practicum.ewm.user.model.dto.UserOutputDto;

import java.util.List;

public interface UserService {
    UserOutputDto createUser(UserInputDto userInputDto);

    void deleteUser(long id);

    List<UserOutputDto> getUsers(List<Long> ids, Integer from, Integer size);
}
