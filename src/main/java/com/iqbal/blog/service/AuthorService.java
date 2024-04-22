package com.iqbal.blog.service;

import com.iqbal.blog.entity.Author;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorService extends UserDetailsService {
    Author loadAuthorById(String authorId);

    Author get(String id);
}
