package com.deliverengine.deliver.service;

import com.deliverengine.deliver.model.entity.User;
import com.deliverengine.deliver.repository.UserRepository;
import com.deliverengine.deliver.service.abstraction.IBaseEntityService;
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
