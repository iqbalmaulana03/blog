package com.iqbal.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RegisterAuthorResponse {

    private String email;

    private List<String> roles;
}
