package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long catId);

    Category getCategory(Long catId);

    List<Category> getAllCategories(Integer from, Integer size);
}