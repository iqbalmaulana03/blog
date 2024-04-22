package com.iqbal.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String id;

    private String token;

    private List<String> parts;
}
