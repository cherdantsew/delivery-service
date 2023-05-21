package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.entity.User;
import com.orderengine.deliver.deliverservice.repository.UserRepository;
import com.orderengine.deliver.deliverservice.service.abstraction.IBaseEntityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IBaseEntityService<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow();
    }

    @Override
    public User findOneById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User saveAndFlush(User entity) {
        return userRepository.saveAndFlush(entity);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
