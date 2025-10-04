package io.hexlet.spring.controller;

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

    @GetMapping(path = "")
    public List<Product> index(
            @RequestParam(defaultValue = "0") Integer min,
            @RequestParam(defaultValue = "" + Integer.MAX_VALUE) Integer max) {

        Sort sort = Sort.by(Sort.Order.asc("price"));
        return productRepository.findByPriceBetween(min, max, sort);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        if (productRepository.findAll().contains(product)) {
            throw new ResourceAlreadyExistsException("Product with title '" + product.getTitle()
                    + "' and price " + product.getPrice() + " already exists");
        }

        return productRepository.save(product);
    }


    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @PutMapping(path = "/{id}")
    public Product update(@PathVariable long id, @RequestBody Product productData) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        product.setTitle(productData.getTitle());
        product.setPrice(productData.getPrice());

        return productRepository.save(product);
    }
    // END

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}