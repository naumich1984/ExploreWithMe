package ru.practicum.ewm.controller._admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static ru.practicum.ewm.model.mapper.CategoryMapper.toCategoryDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryControllerAdmin {

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

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @NotNull Long catId) {
        log.debug("DELETE /admin/categories/{catId}");
        log.debug(" | catId: {}", catId);

        categoryService.deleteCategory(catId);
    }
}
