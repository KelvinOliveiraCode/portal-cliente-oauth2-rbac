package com.kelvinoliveis.portalrbac.controller;

import com.kelvinoliveis.portalrbac.dto.AuthResponse;
import com.kelvinoliveis.portalrbac.dto.LoginRequest;
import com.kelvinoliveis.portalrbac.security.JwtAuthenticationFilter;
import com.kelvinoliveis.portalrbac.security.SecurityConfig;
import com.kelvinoliveis.portalrbac.service.AuthService;
import com.kelvinoliveis.portalrbac.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthController: delega ao AuthService e traduz resultado em HTTP.
 */
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, TokenService.class,
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void should_retornarToken_quandoLoginValido() throws Exception {
        given(authService.autenticar(any(LoginRequest.class)))
                .willReturn(new AuthResponse("jwt-token-123", "ana", java.util.List.of("ADMIN")));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ana\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.username").value("ana"));
    }

    @Test
    void should_retornar400_quandoLoginInvalido() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"senha\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
