package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.CategoryDto;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.logging.CreationLogging;
import ru.practicum.ewm.logging.DeletionLogging;
import ru.practicum.ewm.logging.UpdateLogging;

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
    @CreationLogging
    public CategoryDto createCategory(CategoryDto category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            log.warn("Category was not create because category name must be unique.");
            throw new BadRequestException("Category name must be unique.");
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryFromDto(category)));
    }

    @Override
    @Transactional
    @UpdateLogging
    public CategoryDto updateCategory(CategoryDto category) {
        getCategory(category.getId());
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryFromDto(category)));
    }

    @Override
    @Transactional
    @DeletionLogging
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found."));
        if (eventRepository.getCountEventByCategoryId(catId) != 0) {
            log.warn("For deleting category no event should be associated with the category.");
            throw new BadRequestException("For deleting category no event should be associated with the category.");
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(getPageRequest(from, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id=" + catId + " was not found."));
        return CategoryMapper.toCategoryDto(category);
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }
}