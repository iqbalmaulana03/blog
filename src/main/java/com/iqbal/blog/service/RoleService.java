package com.iqbal.blog.service;

import com.iqbal.blog.constant.EAuthor;
import com.iqbal.blog.entity.Role;

public interface RoleService {

    Role getOrSave(EAuthor role);
}
