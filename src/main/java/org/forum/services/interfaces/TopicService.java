package org.forum.services.interfaces;

import org.forum.entities.Section;
import org.forum.entities.Topic;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TopicService extends CommonService<Topic, Integer>, ValidationService<Topic> {

    void save(Topic topic, Authentication authentication, Section section);

    List<Topic> findAllBySectionId(Integer sectionId);

}