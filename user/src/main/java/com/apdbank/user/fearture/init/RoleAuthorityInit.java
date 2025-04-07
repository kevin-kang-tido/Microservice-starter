package com.apdbank.user.fearture.init;

import com.apdbank.user.domain.Authority;
import com.apdbank.user.domain.Role;
import com.apdbank.user.fearture.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleAuthorityInit {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @PostConstruct
    void initRole() {

        // TODO: set the role and scope for user

        // Auto-generate roles (ADMIN, user )
        if (roleRepository.count() < 1) {

            // Authorities
            Authority normalRead = new Authority();
            normalRead.setName("normal:read");

            Authority normalWrite = new Authority();
            normalWrite.setName("normal:write");

            Authority userRead = new Authority();
            userRead.setName("user:read");

            Authority userWrite = new Authority();
            userWrite.setName("user:write");

            Authority previewerRead = new Authority();
            previewerRead.setName("previewer:read");

            Authority previewerWrite = new Authority();
            previewerWrite.setName("previewer:write");

            Authority adminRead = new Authority();
            adminRead.setName("admin:read");

            Authority adminWrite = new Authority();
            adminWrite.setName("admin:write");


            authorityRepository.saveAll(List.of(
                    normalRead,normalWrite, userRead, userWrite, previewerRead, previewerWrite, adminRead, adminWrite
            ));

            // Roles
            Role admin = new Role();

            admin.setName("ADMIN");
            admin.setAuthorities(List.of(
                    userRead, userWrite, adminRead, adminWrite,normalRead,previewerRead,previewerWrite,adminRead,adminWrite
            ));

            Role user = new Role();
            user.setName("USER");
            user.setAuthorities(List.of(
                    userRead, userWrite
            ));

            Role  normal = new Role();
            normal.setName("NORMAL");
            normal.setAuthorities(List.of(
                    normalRead
            ));

            Role  previewer = new Role();
            previewer.setName("PREVIEWER");
            previewer.setAuthorities(List.of(
                    previewerRead,previewerWrite
            ));

            // save role to repository
            roleRepository.saveAll(List.of(
                    admin, user, normal ,previewer
            ));

        }
    }

}
