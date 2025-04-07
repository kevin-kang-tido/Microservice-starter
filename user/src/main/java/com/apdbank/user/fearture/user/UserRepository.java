package com.apdbank.user.fearture.user;

import com.apdbank.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByUuid(String uuid);

    Optional<User> findUserByUsername(String username);

    Optional<User> findByUuid(String uuid);


    @Modifying
    @Query("UPDATE User as u SET u.isBlocked = TRUE WHERE u.uuid = ?1")
    void blockByUuid(String uuid);

    @Modifying
    @Query("UPDATE User as u SET u.isDeleted = TRUE WHERE u.uuid = ?1")
    void disableByUuid(String uuid);

    @Modifying
    @Query("UPDATE User as u SET u.isDeleted = FALSE WHERE u.uuid = ?1")
    void enableByUuid(String uuid);

}
