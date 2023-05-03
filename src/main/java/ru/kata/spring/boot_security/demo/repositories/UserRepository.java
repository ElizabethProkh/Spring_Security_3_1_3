package ru.kata.spring.boot_security.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    User findByIdWithRoles(@Param("id") Long id);

}
