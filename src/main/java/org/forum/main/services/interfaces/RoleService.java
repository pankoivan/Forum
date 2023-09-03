package org.forum.main.services.interfaces;

import org.forum.main.entities.Role;
import org.forum.main.services.interfaces.common.SimpleSaveService;
import org.forum.main.services.interfaces.common.ValidationService;

public interface RoleService extends SimpleSaveService<Role, Integer>, ValidationService<Role> {

}
