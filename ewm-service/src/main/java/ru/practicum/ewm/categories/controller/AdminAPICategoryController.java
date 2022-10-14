package ru.practicum.ewm.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.CategoryDto;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminAPICategoryController {
    private final CategoryService categoryService;

    /**
     * Добавление новой категории
     *
     * @param category - данные добавляемой категории
     */
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto category) {
        return categoryService.createCategory(category);
    }

    /**
     * Изменение категории
     *
     * @param category - Данные категории для изменения
     */
    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto category) {
        return categoryService.updateCategory(category);
    }

    /**
     * Удаление категории
     *
     * @param catId - id категории
     */
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }
}