package com.apdbank.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 90)
    private String name;

   // define relationship
   @ManyToMany(mappedBy = "roles",cascade = CascadeType.DETACH)
   private List<User> users;

    @ManyToMany
    private List<Authority> authorities;


    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
