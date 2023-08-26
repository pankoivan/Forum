package org.forum.services.interfaces;

import org.forum.entities.Topic;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;

public interface TopicService extends CommonService<Topic, Integer>, ValidationService<Topic> {

}
