package com.kelvinoliveis.portalrbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do portal cliente OAuth2 + RBAC.
 */
@SpringBootApplication
public class PortalClienteOauth2RbacApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalClienteOauth2RbacApplication.class, args);
    }
}
