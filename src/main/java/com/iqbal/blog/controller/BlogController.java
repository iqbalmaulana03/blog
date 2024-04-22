package com.iqbal.blog.controller;

import com.iqbal.blog.model.request.SearchBlogRequest;
import com.iqbal.blog.model.response.BlogResponse;
import com.iqbal.blog.model.request.BlogRequest;
import com.iqbal.blog.model.response.PagingResponse;
import com.iqbal.blog.model.response.WebResponse;
import com.iqbal.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService service;

    @PostMapping
    ResponseEntity<WebResponse<BlogResponse>> create(@RequestBody BlogRequest request){
        BlogResponse blogResponse = service.create(request);

        WebResponse<BlogResponse> response = WebResponse.<BlogResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create blog")
                .data(blogResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{blogId}")
    ResponseEntity<WebResponse<BlogResponse>> get(@PathVariable("blogId") String id){
        BlogResponse blogResponse = service.get(id);

        WebResponse<BlogResponse> response = WebResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get details blog")
                .data(blogResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{blogId}")
    ResponseEntity<WebResponse<String>> delete(@PathVariable("blogId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete blog")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{blogId}")
    ResponseEntity<WebResponse<BlogResponse>> update(@PathVariable("blogId") String id,
                                                     @RequestBody BlogRequest request){
        request.setId(id);

        BlogResponse blogResponse = service.update(request);

        WebResponse<BlogResponse> response = WebResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update blog")
                .data(blogResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WebResponse<List<BlogResponse>>> search(@RequestParam(name = "title", required = false)String title,
                                                                  @RequestParam(name = "body", required = false)String body,
                                                                  @RequestParam(name = "page", defaultValue = "1")Integer page,
                                                                  @RequestParam(name = "size", defaultValue = "10")Integer size){

        SearchBlogRequest request = SearchBlogRequest.builder()
                .title(title)
                .body(body)
                .page(page)
                .size(size)
                .build();

        Page<BlogResponse> search = service.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<List<BlogResponse>> response = WebResponse.<List<BlogResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get blogs")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
