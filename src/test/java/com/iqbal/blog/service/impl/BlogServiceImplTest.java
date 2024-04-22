package com.iqbal.blog.service.impl;

import com.iqbal.blog.entity.Author;
import com.iqbal.blog.entity.Blog;
import com.iqbal.blog.model.request.BlogRequest;
import com.iqbal.blog.model.request.SearchBlogRequest;
import com.iqbal.blog.model.response.BlogResponse;
import com.iqbal.blog.repository.BlogRepository;
import com.iqbal.blog.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @InjectMocks
    private BlogServiceImpl blogService;

    @Mock
    private AuthorServiceImpl authorService;

    @Mock
    private BlogRepository repository;

    @Mock
    private ValidationUtils utils;

    @Test
    void create() {

        BlogRequest request = BlogRequest.builder()
                .title("Existing Blog")
                .body("Testing blog body")
                .authorId("1")
                .build();

        when(repository.findByTitle(request.getTitle())).thenReturn(Optional.of(new Blog()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            blogService.create(request);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title already exist", exception.getReason());


        verify(utils).validate(request);
        verify(repository).findByTitle(request.getTitle());
        verifyNoInteractions(authorService);
    }

    @Test
    void get() {
        String blogId = "1";

        Blog blog = Blog.builder()
                .id(blogId)
                .title("Existing Blog")
                .body("Testing blog body")
                .author(Author.builder().build())
                .build();

        when(repository.findById(blogId)).thenReturn(Optional.of(blog));

        BlogResponse response = blogService.get(blogId);

        verify(repository).findById(blogId);
        assertEquals(blog.getId(), response.getId());
        assertEquals(blog.getTitle(), response.getTitle());
        assertEquals(blog.getBody(), response.getBody());
    }

    @Test
    void delete() {
        String blogId = "1";
        Blog blog = Blog.builder()
                .id(blogId)
                .title("Existing Blog")
                .body("Testing blog body")
                .author(Author.builder().build())
                .build();

        when(repository.findById(blogId)).thenReturn(Optional.of(blog));

        blogService.delete(blogId);

        verify(repository).findById(blogId);
        verify(repository).delete(blog);
    }

    @Test
    void update() {
        BlogRequest request = BlogRequest.builder()
                .id("1")
                .title("New Blog")
                .body("Updated blog body")
                .authorId("1")
                .build();

        Author author = Author.builder()
                .id("1")
                .build();

        Blog existingBlog = Blog.builder()
                .id("1")
                .title("Existing Blog")
                .body("Existing blog body")
                .author(Author.builder().build())
                .build();

        when(repository.findByTitle(request.getTitle())).thenReturn(Optional.empty());

        when(authorService.get(request.getAuthorId())).thenReturn(author);

        when(repository.findById(request.getId())).thenReturn(Optional.of(existingBlog));

        BlogResponse response = blogService.update(request);

        verify(utils).validate(request);

        verify(repository).findByTitle(request.getTitle());

        verify(authorService).get(request.getAuthorId());

        verify(repository).findById(request.getId());

        verify(repository).save(existingBlog);

        assertEquals(existingBlog.getTitle(), response.getTitle());
        assertEquals(existingBlog.getBody(), response.getBody());
        assertEquals(existingBlog.getAuthor().getId(), response.getAuthorId());
        assertEquals(existingBlog.getAuthor().getName(), response.getName());
    }

    @Test
    void search() {

        SearchBlogRequest request = SearchBlogRequest.builder()
                .title("Test")
                .body("Body")
                .page(1)
                .size(10)
                .build();

        Author author = Author.builder()
                .id("1")
                .name("testing")
                .build();

        List<Blog> blogs = new ArrayList<>();
        blogs.add(Blog.builder().id("1").title("Test Blog 1").body("Body 1").author(author).build());
        blogs.add(Blog.builder().id("2").title("Test Blog 2").body("Body 2").author(author).build());
        blogs.add(Blog.builder().id("3").title("Another Blog").body("Body 3").author(author).build());

        Page<Blog> page = new PageImpl<>(blogs, Pageable.unpaged(), 3);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<BlogResponse> responsePage = blogService.search(request);

        verify(repository).findAll(any(Specification.class), any(Pageable.class));

        assertEquals(3, responsePage.getTotalElements());
        assertEquals(1, responsePage.getTotalPages());
        assertEquals(3, responsePage.getContent().size());
        assertEquals("1", responsePage.getContent().get(0).getId());
        assertEquals("Test Blog 1", responsePage.getContent().get(0).getTitle());
        assertEquals("Body 1", responsePage.getContent().get(0).getBody());
        assertEquals("2", responsePage.getContent().get(1).getId());
        assertEquals("Test Blog 2", responsePage.getContent().get(1).getTitle());
        assertEquals("Body 2", responsePage.getContent().get(1).getBody());
        assertEquals("3", responsePage.getContent().get(2).getId());
        assertEquals("Another Blog", responsePage.getContent().get(2).getTitle());
        assertEquals("Body 3", responsePage.getContent().get(2).getBody());
    }
}