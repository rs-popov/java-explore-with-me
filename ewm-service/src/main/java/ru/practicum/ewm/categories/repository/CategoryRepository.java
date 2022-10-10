package ru.practicum.ewm.categories.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @NonNull Page<Category> findAll(@NonNull Pageable page);

    @Query("select c from Category c where upper(c.name)  = upper(?1) ")
    Category findByName(String name);
}