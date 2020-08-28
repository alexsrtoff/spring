package shop.persistance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Autowired
    public ProductRepository(DataSource dataSource) throws SQLException {
        this(dataSource.getConnection());
        createTableIfNotExist();
    }

    public void createTableIfNotExist() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("create table if not exists products (id int auto_increment primary key, title varchar(255), price int);");
        }
    }

    public void update(Product product) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("update products set title = ?, price = ? where id = ?;");
        stmt.setString(1, product.getTitle());
        stmt.setInt(2, product.getPrice());
        stmt.setInt(3, product.getId());
        stmt.execute();
    }

    public List<Product> findAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("select id, title, price from products");

            while (rs.next()) {
                products.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }
        return products;

    }

    public Product findById(long id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select id, title, price from products where id = ?;");
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Product(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        return new Product(-1, "", -1);
    }

    public void addProduct(Product product) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("insert into products(title, price) values (?, ?);");
        stmt.setString(1, product.getTitle());
        stmt.setInt(2, product.getPrice());
        stmt.execute();
    }

    public void deleteOne(Product product) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("delete from products where id =?;");
        stmt.setInt(1, product.getId());
        stmt.execute();
    }
}
