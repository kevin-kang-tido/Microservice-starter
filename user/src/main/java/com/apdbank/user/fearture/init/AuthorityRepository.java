package com.apdbank.user.fearture.init;

import com.apdbank.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
