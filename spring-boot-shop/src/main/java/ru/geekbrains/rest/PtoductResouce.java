package ru.geekbrains.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.NotFoundExeption;
import ru.geekbrains.persist.entity.Product;
import ru.geekbrains.persist.repo.ProductRepository;

import java.util.List;

@RequestMapping("/api/v1/products")
@RestController
public class PtoductResouce {

    private final ProductRepository productRepository;

    @Autowired
    public PtoductResouce(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    @GetMapping(path = "/{id}/id", produces = "application/json")
    public Product findById(@PathVariable("id") int id){
       return productRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }

    @PostMapping(consumes = "application/json")
    public Product createProduct(@RequestBody Product product){

        if (product.getId() != null){
            throw new IllegalArgumentException("Id found in the create reuest");
        }
        productRepository.save(product);
        return product;
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public Product updateProduct(@RequestBody Product product){
        productRepository.save(product);
        return product;
    }

    @DeleteMapping (path = "/{id}/id", produces = "application/json")
    public void deleteById(@PathVariable("id") int id){
        productRepository.deleteById(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExeptionHandler(IllegalArgumentException e){
        return  new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);

    }

}
