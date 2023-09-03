package org.forum.main.services.interfaces;

import org.forum.main.entities.Authority;
import org.forum.main.services.interfaces.common.SimpleSaveService;
import org.forum.main.services.interfaces.common.ValidationService;

public interface AuthorityService extends SimpleSaveService<Authority, Integer>, ValidationService<Authority> {

}
