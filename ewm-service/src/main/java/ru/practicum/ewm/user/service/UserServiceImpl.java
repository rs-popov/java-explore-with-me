package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.config.OffsetLimitPageable;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.logging.CreationLogging;
import ru.practicum.ewm.logging.DeletionLogging;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserInputDto;
import ru.practicum.ewm.user.model.dto.UserMapper;
import ru.practicum.ewm.user.model.dto.UserOutputDto;
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
    @CreationLogging
    public UserOutputDto createUser(UserInputDto input) {
        User user = UserMapper.toUserFromInputDto(input);
        return UserMapper.toUserOutputDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @DeletionLogging
    public void deleteUser(long id) {
        getUser(id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UserOutputDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(OffsetLimitPageable.of(from, size));
        } else if (ids.size() == 1) {
            User user = getUser(ids.get(0));
            return List.of(UserMapper.toUserOutputDto(user));
        } else {
            users = userRepository.getUsersByIds(ids, OffsetLimitPageable.of(from, size));
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

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + userId + " was not found."));
    }
}