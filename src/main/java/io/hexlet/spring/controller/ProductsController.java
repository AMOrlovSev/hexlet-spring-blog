package io.hexlet.spring.controller;

import io.hexlet.spring.dto.product.ProductCreateDTO;
import io.hexlet.spring.dto.product.ProductDTO;
import io.hexlet.spring.dto.product.ProductUpdateDTO;
import io.hexlet.spring.exception.ResourceAlreadyExistsException;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Product;
import io.hexlet.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

//    @GetMapping(path = "")
//    public List<Product> index() {
//        return productRepository.findAll();
//    }

//    @GetMapping(path = "")
//    public List<Product> index(
//            @RequestParam(defaultValue = "0") Integer min,
//            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) Integer max) {
//
//        Sort sort = Sort.by(Sort.Order.asc("price"));
//        return productRepository.findByPriceBetween(min, max, sort);
//    }

    @GetMapping(path = "")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream()
                .map(this::toDTO)
                .toList();
    }

//    @PostMapping(path = "")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Product create(@RequestBody Product product) {
//        if (productRepository.findAll().contains(product)) {
//            throw new ResourceAlreadyExistsException("Product with title '" + product.getTitle()
//                    + "' and price " + product.getPrice() + " already exists");
//        }
//
//        return productRepository.save(product);
//    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productData) {
        var product = toEntity(productData);
        productRepository.save(product);
        var productDto = toDTO(product);
        return productDto;
    }


    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

//    @PutMapping(path = "/{id}")
//    public Product update(@PathVariable long id, @RequestBody Product productData) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
//
//        product.setTitle(productData.getTitle());
//        product.setPrice(productData.getPrice());
//
//        return productRepository.save(product);
//    }


    @PutMapping(path = "/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        toEntity(dto, product);

        productRepository.save(product);

        var productDto = toDTO(product);
        return productDto;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }


    private Product toEntity(ProductUpdateDTO productDto, Product product) {
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return product;
    }

    private Product toEntity(ProductCreateDTO productDto) {
        var product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setVendorCode(productDto.getVendorCode());
        return product;
    }

    private ProductDTO toDTO(Product product) {
        var dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setVendorCode(product.getVendorCode());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}