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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BanRepository banRepository;

    @GetMapping("/test")
    public String testReturn() {
        System.out.println();
        System.out.println(userRepository.findById(1));
        System.out.println();
        System.out.println(userDetailsRepository.findById(1));
        System.out.println();
        System.out.println(sectionRepository.findById(1));
        System.out.println();
        System.out.println(topicRepository.findById(1));
        System.out.println();
        System.out.println(messageRepository.findById(1L));
        System.out.println();
        System.out.println(assignedRoleRepository.findById(1));
        System.out.println();
        System.out.println(roleRepository.findById(1));
        System.out.println();
        System.out.println(banRepository.findById(1));
        System.out.println();
        return "index";
    }

}
