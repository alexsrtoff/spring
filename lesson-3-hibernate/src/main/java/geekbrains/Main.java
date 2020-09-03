package geekbrains;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class Main {

    public static void addUsersAndProducts(EntityManagerFactory emFactory){
        EntityManager em = emFactory.createEntityManager();

        User user1 = new User(null, "alex", "alex");
        User user2 = new User(null, "ivan", "ivan");

        Product product1 = new Product("Phone", 10000);
        Product product2 = new Product("TV", 20000);
        Product product3 = new Product("Radio", 50001);
        Product product4 = new Product("Car", 900002);
        Product product5 = new Product("Bike", 30000);

        em.getTransaction().begin();
        em.persist(user1);
        em.persist(user2);
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);
        em.persist(product4);
        em.persist(product5);
        em.getTransaction().commit();
        em.close();
    }

    public static void main(String[] args) {
        EntityManagerFactory emFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        /*Home task*/
        addUsersAndProducts(emFactory);
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();
        Product product1 = em.find(Product.class, 1);
        Product product2 = em.find(Product.class, 2);
        Product product3 = em.find(Product.class, 3);

        User user1 = em.find(User.class, 1);
        User user2 = em.find(User.class, 2);

        user1.addProduct(product1);
        user1.addProduct(product2);
        user1.addProduct(product3);
        user2.addProduct(product1);
        user2.addProduct(product2);
        em.getTransaction().commit();
        em.close();

        /*выводим список покупок и покупателей*/
        em = emFactory.createEntityManager();
        em.getTransaction().begin();
        List<Product> productsUser1 = user1.getProducts();
        System.out.println("Список покупок user1\n" + productsUser1 + "\n");
        List<Product> productsUser2 = user2.getProducts();
        System.out.println("Список покупок user2\n" + productsUser2 + "\n");
        List<User> usersProduct1 = product1.getUsers();
        System.out.println("Список покупателей товара1\n" + usersProduct1 + "\n");
        em.getTransaction().commit();
        em.close();

        /*удаляем товар или покупателя*/
        em = emFactory.createEntityManager();
        em.getTransaction().begin();
//        em.createQuery("delete from User u  where u.id = 1").executeUpdate();
        em.createQuery("delete from Product p  where p.id = 2").executeUpdate();
        em.getTransaction().commit();
        em.close();

        /*выводим список покупок и покупателей после удаления*/
        em = emFactory.createEntityManager();
        em.getTransaction().begin();
        System.out.println("After delete");
        productsUser1 = user1.getProducts();
        System.out.println("Список покупок user1\n" + productsUser1 + "\n");
        productsUser2 = user2.getProducts();
        System.out.println("Список покупок user2\n" + productsUser2 + "\n");
        usersProduct1 = product1.getUsers();
        System.out.println("Список покупателей товара1\n" + usersProduct1 + "\n");
        em.getTransaction().commit();
        em.close();



        // INSERT
//        EntityManager em = emFactory.createEntityManager();
//        User user1 = new User(null, "alex", "alex");
//        User user2 = new User(null, "ivan", "ivan");
//        User user3 = new User(null, "petr", "petr");
//
//        em.getTransaction().begin();
//        em.persist(user1);
//        em.persist(user2);
//        em.persist(user3);
//        em.getTransaction().commit();
//
//        em.close();

        // SELECT
//        EntityManager em = emFactory.createEntityManager();
//
//        User user = em.find(User.class, 1);
//        System.out.println(user);
//
//        List<User> users = em.createQuery("from User", User.class).getResultList();
//        System.out.println(users);
//
//        user = em.createQuery("from User where login = :login", User.class)
//                .setParameter("login", "petr")
//                .getSingleResult();
//        System.out.println(user);

        // UPDATE
//        EntityManager em = emFactory.createEntityManager();
//
//        User user = em.find(User.class, 1);
//        System.out.println(user);
//
//        em.getTransaction().begin();
//        user.setPassword("new_password");
//        em.getTransaction().commit();
//
//        em.close();

//        EntityManager em = emFactory.createEntityManager();
//
//        User user = em.find(User.class, 1);
//        Contact contact = new Contact(null, "mobile phone", "123456789", user);
//
//        em.getTransaction().begin();
//        em.persist(contact);
//        em.getTransaction().commit();
    }
}
