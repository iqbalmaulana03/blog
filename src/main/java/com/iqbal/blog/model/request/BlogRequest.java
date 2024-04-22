package com.iqbal.blog.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BlogRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "title has not be empty!")
    private String title;

    @NotEmpty(message = "body has not be empty!")
    private String body;

    @NotEmpty(message = "author id has not be empty!")
    private String authorId;
}
