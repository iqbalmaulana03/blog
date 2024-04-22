package com.iqbal.blog.service;

import com.iqbal.blog.model.request.SearchBlogRequest;
import com.iqbal.blog.model.response.BlogResponse;
import com.iqbal.blog.model.request.BlogRequest;
import org.springframework.data.domain.Page;

public interface BlogService {

    BlogResponse create(BlogRequest request);

    BlogResponse get(String id);

    void delete(String id);

    BlogResponse update(BlogRequest request);

    Page<BlogResponse> search(SearchBlogRequest request);
}
