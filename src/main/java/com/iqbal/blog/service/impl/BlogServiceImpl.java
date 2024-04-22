package com.iqbal.blog.service.impl;

import com.iqbal.blog.entity.Author;
import com.iqbal.blog.entity.Blog;
import com.iqbal.blog.model.request.SearchBlogRequest;
import com.iqbal.blog.model.response.BlogResponse;
import com.iqbal.blog.model.request.BlogRequest;
import com.iqbal.blog.repository.BlogRepository;
import com.iqbal.blog.service.AuthorService;
import com.iqbal.blog.service.BlogService;
import com.iqbal.blog.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository repository;

    private final AuthorService authorService;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlogResponse create(BlogRequest request) {

        utils.validate(request);

        Optional<Blog> byTitle = repository.findByTitle(request.getTitle());

        if (byTitle.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title already exist");

        Author author = authorService.get(request.getAuthorId());

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .author(author)
                .build();

        repository.saveAndFlush(blog);

        return toBlogResponse(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponse get(String id) {

        Blog blog = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BLOG NOT_FOUND")
        );

        return toBlogResponse(blog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Blog blog = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BLOg NOT_FOUND")
        );

        repository.delete(blog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlogResponse update(BlogRequest request) {
        utils.validate(request);

        Optional<Blog> byTitle = repository.findByTitle(request.getTitle());

        if (byTitle.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title already exist");

        Author author = authorService.get(request.getAuthorId());

        Blog blog = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BLOg NOT_FOUND")
        );

        blog.setTitle(request.getTitle());
        blog.setBody(request.getBody());
        blog.setAuthor(author);

        repository.save(blog);

        return toBlogResponse(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogResponse> search(SearchBlogRequest request) {
        Specification<Blog> specification = getBlogSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Blog> blogs = repository.findAll(specification, pageable);

        List<BlogResponse> responses = new ArrayList<>();

        for (Blog blog : blogs){
            BlogResponse response = toBlogResponse(blog);
            responses.add(response);
        }
        return new PageImpl<>(responses, pageable, blogs.getTotalElements());
    }

    private static Specification<Blog> getBlogSpecification(SearchBlogRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getTitle())){
                predicates.add(criteriaBuilder.like(root.get("title"), "%"+ request.getTitle()+"%"));
            }

            if (Objects.nonNull(request.getBody())){
                predicates.add(criteriaBuilder.like(root.get("body"), "%"+ request.getBody()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

    private BlogResponse toBlogResponse(Blog blog){
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .body(blog.getBody())
                .authorId(blog.getAuthor().getId())
                .name(blog.getAuthor().getName())
                .build();
    }
}
