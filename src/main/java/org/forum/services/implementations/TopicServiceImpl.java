package org.forum.services.implementations;

import org.forum.entities.Topic;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class TopicServiceImpl implements CommonService<Topic, Integer>, ValidationService<Topic> {

    @Override
    public boolean savingValidation(Topic validatedObject, BindingResult bindingResult) {
        return false;
    }

    @Override
    public String deletingValidation(Topic validatedObject) {
        return null;
    }

    @Override
    public Topic empty() {
        return null;
    }

    @Override
    public boolean isNew(Topic topic) {
        return false;
    }

    @Override
    public Topic findById(Integer id) {
        return null;
    }

    @Override
    public List<Topic> findAll() {
        return null;
    }

    @Override
    public void save(Topic topic) {

    }

    @Override
    public void deleteById(Integer id) {

    }

}
