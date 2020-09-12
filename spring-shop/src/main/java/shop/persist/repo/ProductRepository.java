package shop.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.persist.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
