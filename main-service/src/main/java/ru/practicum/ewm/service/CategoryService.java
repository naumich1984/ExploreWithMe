package ru.practicum.ewm.service;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    Category addCategory(NewCategoryDto newCategoryDto);

    Category updateCategory(NewCategoryDto newCategoryDto, Long catId);

    Category getCategory(Long catId);

    List<Category> getAllCategories(Integer from, Integer size);

    void deleteCategory(Long catId);
}
