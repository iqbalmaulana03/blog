package com.iqbal.blog.service;

import com.iqbal.blog.model.request.AuthRequest;
import com.iqbal.blog.model.request.LoginRequest;
import com.iqbal.blog.model.response.LoginResponse;
import com.iqbal.blog.model.response.RegisterAuthorResponse;

public interface AuthService {

    RegisterAuthorResponse createAuthor(AuthRequest request);

    LoginResponse login(LoginRequest request);
}
