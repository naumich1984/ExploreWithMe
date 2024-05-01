package ru.practicum.ewm.controller._public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.model.dto.CategoryDto;
import ru.practicum.ewm.model.mapper.CategoryMapper;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.model.mapper.CategoryMapper.toCategoryDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategoryPublic(@PathVariable @NotNull Long catId) {
        log.debug("GET /categories/{catId}");
        log.debug(" | catId: {}", catId);

        return ResponseEntity.status(HttpStatus.OK).body(toCategoryDto(categoryService.getCategoryPublic(catId)));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesPublic(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("GET /categories");
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategoriesPublic(from, size)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList()));
    }

}
