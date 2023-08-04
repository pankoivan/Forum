package org.forum.controllers.mvc;

import org.forum.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AssignedRoleRepository assignedRoleRepository;

    @GetMapping("/")
    public String testReturn() {
        System.out.println();
        return "index";
    }

}
