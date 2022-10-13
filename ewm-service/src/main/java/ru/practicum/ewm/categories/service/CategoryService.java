package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto category);

    CategoryDto updateCategory(CategoryDto category);

    void deleteCategory(Long catId);

    Category getCategory(Long catId);

    List<Category> getAllCategories(Integer from, Integer size);
}