package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.model.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto category);

    CategoryDto updateCategory(CategoryDto category);

    void deleteCategory(Long catId);

    CategoryDto getCategory(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);
}