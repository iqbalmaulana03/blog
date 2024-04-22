package com.iqbal.blog.controller;

import com.iqbal.blog.model.request.AuthRequest;
import com.iqbal.blog.model.request.LoginRequest;
import com.iqbal.blog.model.response.LoginResponse;
import com.iqbal.blog.model.response.RegisterAuthorResponse;
import com.iqbal.blog.model.response.WebResponse;
import com.iqbal.blog.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService service;

    @InjectMocks
    private AuthController controller;

    @Test
    void registerCustomer() {
        AuthRequest request = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .build();

        RegisterAuthorResponse authorResponse = RegisterAuthorResponse.builder()
                .email("test@example.com")
                .build();

        when(service.createAuthor(request)).thenReturn(authorResponse);

        ResponseEntity<WebResponse<RegisterAuthorResponse>> responseEntity = controller.registerCustomer(request);

        verify(service).createAuthor(request);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("successfully create author", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(authorResponse, responseEntity.getBody().getData());
    }

    @Test
    void login() {

        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .id("123")
                .token("generated_token")
                .build();

        // Stubbing service.login to return the login response
        when(service.login(request)).thenReturn(loginResponse);

        // Test and verify
        ResponseEntity<WebResponse<LoginResponse>> responseEntity = controller.login(request);

        // Verify that service.login is called with the request
        verify(service).login(request);

        // Verify the content of the returned ResponseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("successfully login", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(loginResponse, responseEntity.getBody().getData());
    }
}