package org.forum.services.interfaces;

import org.forum.entities.Role;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;

public interface RoleService extends CommonService<Role, Integer>, ValidationService<Role> {

}
