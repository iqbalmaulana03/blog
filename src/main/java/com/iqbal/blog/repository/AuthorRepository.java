package com.iqbal.blog.repository;

import com.iqbal.blog.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByEmail(String email);
}
