package com.vastra.request;

import com.vastra.models.Categories;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int quantity;
    private Categories category;
}
