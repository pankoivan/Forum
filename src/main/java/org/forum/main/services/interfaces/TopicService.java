package org.forum.main.services.interfaces;

import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.ValidationService;
import org.forum.main.services.interfaces.common.CommonService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TopicService extends CommonService<Topic, Integer>, ValidationService<Topic> {

    void save(Topic topic, Authentication authentication, Section section);

    List<Topic> findAllBySectionId(Integer sectionId);

}
