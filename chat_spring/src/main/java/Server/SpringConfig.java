package Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
@Configuration
@ComponentScan(basePackages = "Server")
public class SpringConfig {

//    @Bean
//    public DBService dbService(DataSource dataSource) throws SQLException {
//        return new DBService(dataSource);
//    }

//    @Bean
//    public MainServ mainServ(DBService dbService){
//        return new MainServ(dbService);
//    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("09102007");
        ds.setUrl("jdbc:mysql://localhost:3306/users?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false");
        return ds;
    }
}
