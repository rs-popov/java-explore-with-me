package ru.practicum.ewm.categories.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.categories.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @NonNull Page<Category> findAll(@NonNull Pageable page);

    List<Category> findByName(String name);
}