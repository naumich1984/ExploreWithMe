package ru.practicum.ewm.service;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    Category addCategoryAdmin(NewCategoryDto newCategoryDto);

    Category updateCategoryAdmin(NewCategoryDto newCategoryDto, Long catId);

    Category getCategoryPublic(Long catId);

    List<Category> getAllCategoriesPublic(Integer from, Integer size);

    void deleteCategoryAdmin(Long catId);
}
