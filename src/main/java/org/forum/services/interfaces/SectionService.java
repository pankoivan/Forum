package org.forum.services.interfaces;

import org.forum.entities.Section;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;

public interface SectionService extends CommonService<Section, Integer>, ValidationService<Section> {

}
