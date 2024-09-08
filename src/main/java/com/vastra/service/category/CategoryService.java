package com.vastra.service.category;

import com.vastra.exceptions.AlreadyExistsException;
import com.vastra.exceptions.ResourceNotFoundException;
import com.vastra.models.Categories;
import com.vastra.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Categories getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category doesn't exist"));

    }

    @Override
    public Categories getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories addCategory(Categories category) {

        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save).orElseThrow(() -> new AlreadyExistsException(category.getName() + " already exists"));
    }

    @Override
    public Categories updateCategory(Categories category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);

        }).orElseThrow(() -> new ResourceNotFoundException("Category doesn't exist"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).
                ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }
}

