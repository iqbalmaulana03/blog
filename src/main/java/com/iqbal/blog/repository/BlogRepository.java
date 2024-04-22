package com.iqbal.blog.repository;

import com.iqbal.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, String>, JpaSpecificationExecutor<Blog> {
    Optional<Blog> findByTitle(String title);
}
