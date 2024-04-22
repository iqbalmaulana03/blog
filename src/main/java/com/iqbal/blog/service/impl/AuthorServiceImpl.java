package com.iqbal.blog.service.impl;

import com.iqbal.blog.entity.Author;
import com.iqbal.blog.repository.AuthorRepository;
import com.iqbal.blog.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Author loadAuthorById(String authorId) {
        return repository.findById(authorId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Author get(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email UNAUTHORIZED")
        );
    }
}
