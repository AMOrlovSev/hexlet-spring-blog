package io.hexlet.spring.repository;

import io.hexlet.spring.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceLessThan(Integer price);
    List<Product> findByPriceLessThanEqual(Integer price);
    List<Product> findByPriceGreaterThan(Integer price);
    List<Product> findByPriceGreaterThanEqual(Integer price);

    List<Product> findByPriceBetween(Integer startPrice, Integer endPrice);
    List<Product> findByPriceBetween(Integer min, Integer max, Sort sort);

    List<Product> findByPriceIn(Collection<Integer> prices);

    List<Product> findByPriceBetweenOrderByPriceAsc(int minPrice, int maxPrice);
}
