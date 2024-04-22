package com.iqbal.blog.controller;

import com.iqbal.blog.model.request.BlogRequest;
import com.iqbal.blog.model.request.SearchBlogRequest;
import com.iqbal.blog.model.response.BlogResponse;
import com.iqbal.blog.model.response.WebResponse;
import com.iqbal.blog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    @Mock
    private BlogService service;

    @InjectMocks
    private BlogController controller;

    @Test
    void create() {
        BlogRequest request = BlogRequest.builder()
                .title("Test Blog")
                .body("Testing blog body")
                .authorId("1")
                .build();

        BlogResponse blogResponse = BlogResponse.builder()
                .id("1")
                .title("Test Blog")
                .body("Testing blog body")
                .authorId("1")
                .name("Author Name")
                .build();

        when(service.create(request)).thenReturn(blogResponse);

        ResponseEntity<WebResponse<BlogResponse>> responseEntity = controller.create(request);

        verify(service).create(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("successfully create blog", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(blogResponse, responseEntity.getBody().getData());
    }

    @Test
    void get() {
        String blogId = "1";
        BlogResponse blogResponse = BlogResponse.builder()
                .id(blogId)
                .title("Test Blog")
                .body("Testing blog body")
                .authorId("1")
                .name("Author Name")
                .build();

        when(service.get(blogId)).thenReturn(blogResponse);

        ResponseEntity<WebResponse<BlogResponse>> responseEntity = controller.get(blogId);

        verify(service).get(blogId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("successfully get details blog", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(blogResponse, responseEntity.getBody().getData());
    }

    @Test
    void delete() {

        String blogId = "1";

        ResponseEntity<WebResponse<String>> responseEntity = controller.delete(blogId);

        verify(service).delete(blogId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("successfully delete blog", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals("OK", responseEntity.getBody().getData());
    }

    @Test
    void update() {

        String blogId = "1";
        BlogRequest request = BlogRequest.builder()
                .id(blogId)
                .title("Updated Blog Title")
                .body("Updated blog body")
                .authorId("1")
                .build();

        BlogResponse updatedResponse = BlogResponse.builder()
                .id(blogId)
                .title("Updated Blog Title")
                .body("Updated blog body")
                .authorId("1")
                .name("Author Name")
                .build();

        when(service.update(request)).thenReturn(updatedResponse);

        ResponseEntity<WebResponse<BlogResponse>> responseEntity = controller.update(blogId, request);

        verify(service).update(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("successfully update blog", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(updatedResponse, responseEntity.getBody().getData());
    }

    @Test
    void search() {

        String title = "Test Title";
        String body = "Test Body";
        Integer page = 1;
        Integer size = 10;

        SearchBlogRequest request = SearchBlogRequest.builder()
                .title(title)
                .body(body)
                .page(page)
                .size(size)
                .build();

        List<BlogResponse> blogResponses = new ArrayList<>();
        blogResponses.add(BlogResponse.builder().id("1").title("Blog 1").body("Body 1").authorId("1").name("Author 1").build());
        blogResponses.add(BlogResponse.builder().id("2").title("Blog 2").body("Body 2").authorId("2").name("Author 2").build());

        Page<BlogResponse> pageResponse = new PageImpl<>(blogResponses);

        when(service.search(request)).thenReturn(pageResponse);

        ResponseEntity<WebResponse<List<BlogResponse>>> responseEntity = controller.search(title, body, page, size);

        verify(service).search(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("successfully get blogs", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(blogResponses, responseEntity.getBody().getData());
    }
}