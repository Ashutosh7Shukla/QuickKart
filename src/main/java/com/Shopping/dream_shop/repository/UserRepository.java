package com.Shopping.dream_shop.repository;

import com.Shopping.dream_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
