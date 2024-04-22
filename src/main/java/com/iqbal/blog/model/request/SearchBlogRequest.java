package com.iqbal.blog.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchBlogRequest {

    private String title;

    private String body;

    private Integer size;

    private Integer page;
}
