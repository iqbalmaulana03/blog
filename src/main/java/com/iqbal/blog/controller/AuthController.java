package com.iqbal.blog.controller;

import com.iqbal.blog.model.request.AuthRequest;
import com.iqbal.blog.model.request.LoginRequest;
import com.iqbal.blog.model.response.LoginResponse;
import com.iqbal.blog.model.response.RegisterAuthorResponse;
import com.iqbal.blog.model.response.WebResponse;
import com.iqbal.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/registerAuthor")
    public ResponseEntity<WebResponse<RegisterAuthorResponse>> registerCustomer(@RequestBody AuthRequest request){
        RegisterAuthorResponse author = service.createAuthor(request);

        WebResponse<RegisterAuthorResponse> response = WebResponse.<RegisterAuthorResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create author")
                .data(author)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        LoginResponse login = service.login(request);

        WebResponse<LoginResponse> response = WebResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully login")
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }
}
