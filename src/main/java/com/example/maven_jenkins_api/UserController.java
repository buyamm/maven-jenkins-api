package com.example.maven_jenkins_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public @ResponseBody String createUser(@RequestParam String name){
        User n = new User();
        n.setName(name);
        userRepository.save(n);
        return "Saved a user";

    }
}
