package com.iqbal.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BlogResponse {

    private String id;

    private String title;

    private String body;

    private String authorId;

    private String name;
}
