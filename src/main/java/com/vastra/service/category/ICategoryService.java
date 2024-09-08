package com.vastra.service.category;


import com.vastra.models.Categories;

import java.util.List;

public interface ICategoryService {
    Categories getCategoryById(Long id);

    Categories getCategoryByName(String name);

    List<Categories> getAllCategories();

    Categories addCategory(Categories category);

    Categories updateCategory(Categories category, Long id);

    void deleteCategory(Long id);


}
