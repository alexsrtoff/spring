package shop.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.persist.entity.Product;
import shop.persist.repo.ProductRepository;
import shop.persist.repo.ProductSpecification;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
                                  @RequestParam(value = "showproduct", required = false) String showproduct,
                                  @RequestParam (value = "page")Optional<Integer> page,
                                  @RequestParam (value = "size")Optional<Integer> size,
                                  @RequestParam(value = "sort", required = false) String srt){
        logger.info("Filtering by name; {}", title);

        Sort sort;

        if(srt != null){
            if (srt.startsWith("id")){
                sort = Sort.by("id").ascending();
            }else if (srt.startsWith("title")){
                sort = Sort.by("title").ascending();
            }else if (srt.startsWith("price")){
                sort = Sort.by("price").ascending();
            }else {
                sort = Sort.by("title").ascending();
            }
        }else {
            sort = Sort.by("title").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, size.orElse(5), sort);;

        Specification specification = ProductSpecification.trueLiteral();

        if (title != null && !title.isEmpty()){
           specification = specification.and(ProductSpecification.titleLike(title));
        }

        if(minprice != null || maxprice != null){
            specification = specification.and(ProductSpecification.priceLike(minprice, maxprice));
        }

        if ((title != null) && !title.isEmpty() && minprice != null && maxprice != null){
            specification = specification.and(ProductSpecification.findByPriceAndTitle(title, minprice, maxprice));
        }

        if(minprice != null && maxprice != null){
            specification = specification.and(ProductSpecification.priceBetween(minprice, maxprice));
        }
        model.addAttribute("products", productRepository.findAll(specification, pageRequest));

//        List<Product> products;
//        if(showproduct != null){
//            if (showproduct.equals("Find products with min price")) {
//                products = productRepository.findProductByMinPrice();
//                model.addAttribute("products", products);
//            }else if (showproduct.equals("Find products with max price")) {
//                products = productRepository.findProductByMaxPrice();
//                model.addAttribute("products", products);
//            }else if (showproduct.equals("Find products with min&max price")) {
//                products = productRepository.findProductByMaxAndMinPrice();
//                model.addAttribute("products", products);
//            }
//        }
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
