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
import java.math.BigDecimal;
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
    public String showAllProducts(Model model,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "minprice", required = false) BigDecimal minprice,
                                  @RequestParam(value = "maxprice", required = false) BigDecimal maxprice,
                                  @RequestParam(value = "showproduct", required = false) String showproduct){
        logger.info("Filtering by name; {}", title);
        List<Product> products;

        if((title == null || title.isEmpty()) & (maxprice == null & minprice == null)){
            products = productRepository.findAll();
        }else{
            if (title == null || title.isEmpty() & (maxprice !=null & minprice != null)){
               products = productRepository.findByPriceIsGreaterThanEqualAndPriceIsLessThanEqual(minprice, maxprice);
            }else if (title == null || title.isEmpty() & (maxprice !=null & minprice == null)){
                products = productRepository.findByPriceIsLessThanEqual(maxprice);
            }else if (title == null || title.isEmpty() & (maxprice == null & minprice != null)){
                products = productRepository.findByPriceIsGreaterThanEqual(minprice);
            }else if (title != null & (maxprice !=null & minprice != null)){
                products = productRepository.findByTitleLikeAndPriceGreaterThanEqualAndPriceLessThanEqual("%" + title + "%", minprice, maxprice);
            }else if (title != null & (maxprice == null & minprice != null)){
                products = productRepository.findByTitleLikeAndPriceIsGreaterThanEqual("%" + title + "%", minprice);
            }else if (title != null & (maxprice !=null & minprice == null)){
                products = productRepository.findByTitleLikeAndPriceIsLessThanEqual("%" + title + "%", maxprice);
            }
            else {
                products = productRepository.findByTitleLike("%" + title + "%");
            }
        }
        model.addAttribute("products", products);

        if(showproduct != null) {
            if (showproduct.equals("Find products with min price")) {
                products = productRepository.findProductByMinPrice();
                model.addAttribute("products", products);
            }else if (showproduct.equals("Find products with max price")) {
                products = productRepository.findProductByMaxPrice();
                model.addAttribute("products", products);
            }else if (showproduct.equals("Find products with min&max price")) {
                products = productRepository.findProductByMaxAndMinPrice();
                model.addAttribute("products", products);
            }
        }
        return "products";
    }

    @GetMapping("/filterbyprice/{price}")
    public String findeByMinPrice( @PathVariable("price") Integer price, Model model){
        List<Product> products;
        if (price == 0) {
            products = productRepository.findProductByMinPrice();
        }else if (price == 1){
            products = productRepository.findProductByMaxPrice();
        }else {
            products = productRepository.findProductByMaxAndMinPrice();
        }
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
