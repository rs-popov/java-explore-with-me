package ru.practicum.ewm.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicAPICategoryController {
    private final CategoryService categoryService;

    /**
     * Получение категорий
     *
     * @param from - количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size - количество категорий в наборе
     */
    @GetMapping
    public List<Category> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    /**
     * Получение информации о категории по её идентификатору
     *
     * @param catId - id категории
     */
    @GetMapping("/{catId}")
    public Category getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}