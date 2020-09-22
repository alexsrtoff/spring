package ru.geekbrains.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geekbrains.persist.entity.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    List<Product> findByTitleLike(String title);

    List<Product> findByPriceIsGreaterThanEqualAndPriceIsLessThanEqual(BigDecimal minprice, BigDecimal maxprice);

    List<Product> findByPriceIsLessThanEqual(BigDecimal maxprice);

    List<Product> findByPriceIsGreaterThanEqual(BigDecimal minprice);

    List<Product> findByTitleLikeAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, BigDecimal minprice, BigDecimal maxprice);

    List<Product> findByTitleLikeAndPriceIsGreaterThanEqual(String title, BigDecimal minprice);

    List<Product> findByTitleLikeAndPriceIsLessThanEqual(String title, BigDecimal maxprice);


    @Query("select p from Product p where p.price =(select max (p.price) from Product p)")
    List<Product> findProductByMaxPrice();

    @Query("select p from Product p where p.price =(select min (p.price) from Product p)")
    List<Product> findProductByMinPrice();

    @Query("select p from Product p where (p.price =(select max (p.price) from Product p) or p.price =(select min (p.price) from Product p))")
    List<Product> findProductByMaxAndMinPrice();


}
