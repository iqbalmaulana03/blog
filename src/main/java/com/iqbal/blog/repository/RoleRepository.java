package com.iqbal.blog.repository;

import com.iqbal.blog.constant.EAuthor;
import com.iqbal.blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByPart(EAuthor author);
}
