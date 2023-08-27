package org.forum.services.interfaces;

import org.forum.entities.Authority;
import org.forum.services.interfaces.common.SimpleSaveService;
import org.forum.services.interfaces.common.ValidationService;

public interface AuthorityService extends SimpleSaveService<Authority, Integer>, ValidationService<Authority> {

}
