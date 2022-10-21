package ru.practicum.ewm.user.model.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUserFromInputDto(UserInputDto userInputDto) {
        return User.builder()
                .name(userInputDto.getName())
                .email(userInputDto.getEmail())
                .build();
    }

    public static UserOutputDto toUserOutputDto(User user) {
        return UserOutputDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortOutputDto toUserShortOutputDto(User user) {
        return UserShortOutputDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}