package ru.geekbrains.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.persist.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    List<User> findByLogin(String login);

    List<User> findByLoginLike(String loginPattern);

    @Query("from User u " +
            "where (u.login = :login or :login is null)")
    List<User> queryByEmailLikeAndLoginLike(@Param("login") String login);

    User findOneByLogin(String login);
}
