package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category addCategoryAdmin(NewCategoryDto newCategoryDto) {
        log.debug("RUN addCategory");

        return categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
    }

    @Override
    @Transactional
    public Category updateCategoryAdmin(NewCategoryDto newCategoryDto, Long catId) {
        log.debug("RUN updateCategory");
        getCategoryPublic(catId);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        category.setId(catId);

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryPublic(Long catId) {
        log.debug("RUN getCategory");

        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    @Override
    public List<Category> getAllCategoriesPublic(Integer from, Integer size) {
        log.debug("RUN getAllCategories");
        CommonPageRequest pageable = new CommonPageRequest(from, size);

        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public void deleteCategoryAdmin(Long catId) {
        log.debug("RUN deleteCategory");
        categoryRepository.deleteById(catId);
    }
}
