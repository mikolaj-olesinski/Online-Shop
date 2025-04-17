package org.example.produktylist.Utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtils {

    public static boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}

