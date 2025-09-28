package com.phegon.phegonbank.role.services;

import com.phegon.phegonbank.res.Response;
import com.phegon.phegonbank.role.entity.Role;

import java.util.List;

public interface RoleService {

    Response<Role> createRole(Role roleRequest);

    Response<Role> updateRole(Role roleRequest);

    Response<List<Role>> getAllRoles();

    Response<?> deleteRole(Long id);

}
