package shop.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.persist.entity.Product;
import shop.persist.repo.ProductRepository;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public String showAllProducts(Model model, @RequestParam(value = "title", required = false) String title){
        logger.info("Filtering by name; {}", title);
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/update/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model){
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid Product product, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "product";
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/addproduct")
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return "product";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) throws SQLException {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}
