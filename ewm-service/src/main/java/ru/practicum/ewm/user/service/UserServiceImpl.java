package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.user.dto.UserInputDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserOutputDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserOutputDto createUser(UserInputDto input) {
        User user = UserMapper.toUserFromInputDto(input);
        return UserMapper.toUserOutputDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + id + " was not found."));
        userRepository.deleteById(id);
        log.info("Удален  профиль пользователя , id={}", id);
    }

    @Override
    @Transactional
    public List<UserOutputDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(getPageRequest(from, size));
        } else if (ids.size() == 1) {
            User user = userRepository.findById(ids.get(0))
                    .orElseThrow(() -> new ObjectNotFoundException("User with id=" + ids.get(0) + " was not found."));
            return List.of(UserMapper.toUserOutputDto(user));
        } else {
            users = userRepository.getUsersByIds(ids, getPageRequest(from, size));
            if (users.isEmpty()) {
                throw new ObjectNotFoundException("Users with ids=" + ids + " was not found.");
            }
            users.stream().forEach(user -> ids.remove(user.getId()));
            if (!ids.isEmpty()) {
                log.warn("Users with id={} were not found", ids);
            }
        }
        return users.stream()
                .map(UserMapper::toUserOutputDto)
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }
}