package org.forum.services.interfaces;

import org.forum.entities.Authority;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;

public interface AuthorityService extends CommonService<Authority, Integer>, ValidationService<Authority> {

}
