package com.vastra.service.product;

import com.vastra.exceptions.ProductNotFoundException;
import com.vastra.models.Categories;
import com.vastra.models.Product;
import com.vastra.repository.CategoryRepository;
import com.vastra.repository.ProductRepository;
import com.vastra.request.AddProductRequest;
import com.vastra.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        /*
         * Check if the category is found in the database.
         * If yes, set it as new product.
         * if no, save it as new category then set it as new product in that category.
         */
        Categories category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Categories newCategory = new Categories(request.getCategory().getName());
                    return categoryRepository.save(newCategory);

                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    //    Helper Methods
    private Product createProduct(AddProductRequest request, Categories category) {
        return new Product(
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                request.getPrice(),
                request.getQuantity(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Doesn't Exist!!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product Not Found!!");
        });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product Does Not Exist!!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest updateRequest) {

        existingProduct.setName(updateRequest.getName());
        existingProduct.setDescription(updateRequest.getDescription());
        existingProduct.setBrand(updateRequest.getBrand());
        existingProduct.setPrice(updateRequest.getPrice());
        existingProduct.setQuantity(updateRequest.getQuantity());

        Categories category = categoryRepository.findByName(updateRequest.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
