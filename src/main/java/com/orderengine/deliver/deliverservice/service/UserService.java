package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.entity.User;
import com.orderengine.deliver.deliverservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow();
    }

    public void save(User user){
        userRepository.save(user);
    }
}
