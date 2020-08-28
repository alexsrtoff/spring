package shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.persistance.Product;
import shop.persistance.ProductRepository;

import java.sql.SQLException;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public String showAllProducts(Model model) throws SQLException {
        model.addAttribute("products", productRepository.findAllProducts());
        return "products";
    }

    @GetMapping("/update/{id}")
    public String editProduct(@PathVariable("id") long id, Model model) throws SQLException {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "product";
    }


    @PostMapping("/update")
    public String updateProduct(Product product) throws SQLException {
        productRepository.update(product);
        return "redirect:/products";
    }

    @GetMapping("/addproduct")
    public String addProduct(){
        return "addproduct";
    }

    @PostMapping("/addproduct")
    public String add(Product product) throws SQLException {
        productRepository.addProduct(product);
        return "redirect:/products";
    }


/*наверно не совсем правильно так писать delete, но вроде работает :)*/
    @GetMapping("/deleteone/{id}")
    public String deleteProduct(@PathVariable("id") long id) throws SQLException {
        Product product = productRepository.findById(id);
        productRepository.deleteOne(product);
        return "redirect:/products";
    }
}
