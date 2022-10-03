package ru.practicum.ewm.user.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull Page<User> findAll(@NonNull Pageable page);

    @Query("select u from User u where u.id in ?1 order by u.id asc ")
    Page<User> getUsersByIds(List<Long> ids, Pageable pageable);
}