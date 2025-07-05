package com.iam.iam_app.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.iam.iam_app.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByEmail(String email);
}
