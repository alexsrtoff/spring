package ru.geekbrains.persist.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.entity.Product;

import java.math.BigDecimal;

public final class ProductSpecification {

    public static Specification<Product> trueLiteral(){
        return (root, qwery, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<Product> titleLike(String title){
        return (root, qwery, builder) -> builder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Product> priceLike(BigDecimal price1, BigDecimal price2){
        return  (root, qwery, builder) -> builder.or(builder.greaterThanOrEqualTo(root.get("price"), price1), builder.lessThanOrEqualTo(root.get("price"), price2));
    }

    public static Specification<Product> priceBetween(BigDecimal minprice, BigDecimal maxprice){
        return (root, qwery, builder) -> builder.and(builder.greaterThanOrEqualTo(root.get("price"), minprice), builder.lessThanOrEqualTo(root.get("price"), maxprice));
    }

    public static Specification<Product> findByPriceAndTitle(String title, BigDecimal minprice, BigDecimal maxprice){
        return (root, qwery, builder) -> builder.and(builder.like(root.get("title"), "%" + title + "%"), builder.greaterThanOrEqualTo(root.get("price"), minprice), builder.lessThanOrEqualTo(root.get("price"), maxprice));
    }
}
