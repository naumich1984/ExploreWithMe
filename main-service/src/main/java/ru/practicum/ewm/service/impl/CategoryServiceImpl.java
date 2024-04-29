package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.dto.NewCategoryDto;
import ru.practicum.ewm.model.mapper.CategoryMapper;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.utility.CommonPageRequest;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category addCategory(NewCategoryDto newCategoryDto) {
        log.debug("RUN addCategory");

        return categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
    }

    @Override
    @Transactional
    public Category updateCategory(NewCategoryDto newCategoryDto, Long catId) {
        log.debug("RUN updateCategory");
        getCategory(catId);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(catId);

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Long catId) {
        log.debug("RUN getCategory");
        Optional<Category> categoryO = categoryRepository.findById(catId);
        if (categoryO.isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }

        return categoryO.get();
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        log.debug("RUN getAllCategories");
        CommonPageRequest pageable = new CommonPageRequest(from, size);
        Page<Category> categories = categoryRepository.findAll(pageable);

        return categories.getContent();
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.debug("RUN deleteCategory");
        categoryRepository.deleteById(catId);
    }
}
