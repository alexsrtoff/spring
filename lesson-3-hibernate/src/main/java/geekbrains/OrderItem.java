package geekbrains;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Column
    private BigDecimal price;

    @Column
    private Integer count;

    public OrderItem() {
    }

    public OrderItem(Integer id, User user, Product product, BigDecimal price, Integer count) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.price = price;
        this.count = count;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", price=" + price +
                ", count=" + count +
                ", product=" + product +
                ", user=" + user +
                '}';
    }
}
