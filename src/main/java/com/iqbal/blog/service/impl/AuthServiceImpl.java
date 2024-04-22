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
import com.iqbal.blog.service.AuthService;
import com.iqbal.blog.service.RoleService;
import com.iqbal.blog.utils.ValidationUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthorRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ValidationUtils validationUtils;
    private final RoleService roleService;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin(){

        String emailSuper = "superadmin@gmail.com";

        Optional<Author> optionalUser = repository.findByEmail(emailSuper);

        if (optionalUser.isPresent()) return;

        Role roleSuperAdmin = roleService.getOrSave(EAuthor.ROLE_ADMIN);

        Role roleAuthor = roleService.getOrSave(EAuthor.ROLE_AUTHOR);

        String encode = passwordEncoder.encode("P@ssword9898");

        Author user = Author.builder()
                .email(emailSuper)
                .password(encode)
                .name("ADMIN_IQBAL")
                .roles(List.of(roleSuperAdmin, roleAuthor))
                .build();
        repository.save(user);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterAuthorResponse createAuthor(AuthRequest request) {
        validationUtils.validate(request);

        Optional<Author> byEmail = repository.findByEmail(request.getEmail());

        if (byEmail.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already exist");

        Role roleAuthor = roleService.getOrSave(EAuthor.ROLE_AUTHOR);

        String encode = passwordEncoder.encode(request.getPassword());

        Author user = Author.builder()
                .email(request.getEmail())
                .password(encode)
                .name(request.getName())
                .roles(List.of(roleAuthor))
                .build();

        repository.saveAndFlush(user);

        return toRegisterResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtils.validate(request);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Author user = (Author) authentication.getPrincipal();

        String token = jwtUtils.generateToken(user);
        List<String> parts = user.getRoles().stream().map(part -> part.getPart().name()).toList();

        return LoginResponse.builder()
                .id(user.getId())
                .token(token)
                .parts(parts)
                .build();
    }

    private RegisterAuthorResponse toRegisterResponse(Author user){
        List<String> parts = user.getRoles().stream().map(part -> part.getPart().name()).toList();

        return RegisterAuthorResponse.builder()
                .email(user.getEmail())
                .roles(parts)
                .build();
    }
}
