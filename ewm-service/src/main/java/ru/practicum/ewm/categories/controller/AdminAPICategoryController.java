package ru.practicum.ewm.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
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
    public Category createCategory(@RequestBody @Valid Category category) {
        return categoryService.createCategory(category);
    }

    /**
     * Изменение категории
     *
     * @param category - Данные категории для изменения
     */
    @PatchMapping
    public Category updateCategory(@RequestBody @Valid Category category) {
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