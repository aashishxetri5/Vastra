package com.vastra.repository;

import com.vastra.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String name);

    boolean existsByName(String name);
}
