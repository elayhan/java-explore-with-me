package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.util.PageableCreator.getPageable;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = mapper.categoryDtoToCategory(categoryDto);
        return mapper.categoryToCategoryDto(repository.save(category));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        getCategoryById(categoryId);
        repository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        CategoryDto updateCategory = getCategoryById(categoryId);
        updateCategory.setName(categoryDto.getName());
        return addCategory(updateCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        Pageable pageable = getPageable(from, size);
        return mapper.categoryListToCategoryDtoList(repository.getAllCategories(pageable));
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return repository.findById(catId)
                .map(mapper::categoryToCategoryDto)
                .orElseThrow(() -> new NotFoundException("Category was id=" + catId + " not found"));
    }
}
