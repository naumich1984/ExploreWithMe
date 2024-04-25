package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.dto.mapper.CategoryMapper;
import ru.practicum.ewm.model.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.model.dto.mapper.CategoryMapper.toCategoryDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.debug("POST /admin/categories");
        log.debug(" | name: {}", newCategoryDto.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toCategoryDto(categoryService.addCategory(newCategoryDto)));
    }

    @PatchMapping("/admin/categories/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid NewCategoryDto newCategoryDto,
                                                      @PathVariable @NotNull Long catId) {
        log.debug("PATCH /admin/categories/{catId}");
        log.debug(" | catId: {}", catId);
        log.debug(" | name: {}", newCategoryDto.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(toCategoryDto(categoryService.updateCategory(newCategoryDto, catId)));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable @NotNull Long catId) {
        log.debug("GET /categories/{catId}");
        log.debug(" | catId: {}", catId);

        return ResponseEntity.status(HttpStatus.OK).body(toCategoryDto(categoryService.getCategory(catId)));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("GET /categories");
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories(from, size)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull Long catId) {
        log.debug("DELETE /admin/categories/{catId}");
        log.debug(" | catId: {}", catId);

        categoryService.deleteCategory(catId);
    }
}
