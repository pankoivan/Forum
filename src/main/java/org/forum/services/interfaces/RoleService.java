package org.forum.services.interfaces;

import org.forum.entities.Role;
import org.forum.services.interfaces.common.SimpleSaveService;
import org.forum.services.interfaces.common.ValidationService;

public interface RoleService extends SimpleSaveService<Role, Integer>, ValidationService<Role> {

}
