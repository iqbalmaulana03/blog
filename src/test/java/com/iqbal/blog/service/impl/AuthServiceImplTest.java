package com.iqbal.blog.service.impl;

import com.iqbal.blog.constant.EAuthor;
import com.iqbal.blog.entity.Author;
import com.iqbal.blog.entity.Role;
import com.iqbal.blog.model.request.AuthRequest;
import com.iqbal.blog.model.request.LoginRequest;
import com.iqbal.blog.model.response.LoginResponse;
import com.iqbal.blog.model.response.RegisterAuthorResponse;
import com.iqbal.blog.repository.AuthorRepository;
import com.iqbal.blog.security.JwtUtils;
import com.iqbal.blog.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthorRepository repository;

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    void createAuthor() {

        AuthRequest request = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .build();

        Role roleAuthor = Role.builder().part(EAuthor.ROLE_AUTHOR).build();

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(roleService.getOrSave(EAuthor.ROLE_AUTHOR)).thenReturn(roleAuthor);

        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        RegisterAuthorResponse response = authService.createAuthor(request);

        verify(validationUtils).validate(request);

        verify(repository).findByEmail(request.getEmail());

        verify(roleService).getOrSave(EAuthor.ROLE_AUTHOR);

        verify(passwordEncoder).encode(request.getPassword());

        verify(repository).saveAndFlush(any(Author.class));

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
    }

    @Test
    void login() {

        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        Author user = new Author();

        List<Role> roles = new ArrayList<>();
        user.setId("123");
        user.setEmail(request.getEmail());
        user.setPassword("encoded_password");
        user.setRoles(roles);

        doNothing().when(validationUtils).validate(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        String token = "generated_token";
        when(jwtUtils.generateToken(user)).thenReturn(token);

        LoginResponse response = authService.login(request);

        verify(validationUtils).validate(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(jwtUtils).generateToken(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(token, response.getToken());
    }
}