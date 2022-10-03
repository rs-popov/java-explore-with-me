package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).size() != 0) {
            throw new BadRequestException("Category name must be unique");
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        categoryRepository.findById(category.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + category.getId() + " was not found."));
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found."));
        if (eventRepository.getCountEventByCategoryId(catId) != 0) {
            throw new BadRequestException("No event should be associated with the category");
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(getPageRequest(from, size)).stream()
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found."));
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }
}