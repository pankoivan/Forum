package org.forum.main.services.interfaces;

import org.forum.main.entities.Section;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.springframework.security.core.Authentication;

public interface SectionService extends GeneralService<Section, Integer>, ValidationService<Section> {

    void save(Section section, Authentication authentication);

}
