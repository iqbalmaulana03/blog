package com.iqbal.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class JwtClaim {

    private String userId;

    private List<String> roles;
}
