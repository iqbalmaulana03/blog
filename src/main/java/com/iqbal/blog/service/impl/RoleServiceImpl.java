package com.iqbal.blog.service.impl;

import com.iqbal.blog.constant.EAuthor;
import com.iqbal.blog.entity.Role;
import com.iqbal.blog.repository.RoleRepository;
import com.iqbal.blog.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public Role getOrSave(EAuthor role) {
        Optional<Role> optionalRole = repository.findByPart(role);

        if (optionalRole.isPresent()) return optionalRole.get();

        Role roles = Role.builder()
                .part(role)
                .build();

        return repository.save(roles);
    }
}
