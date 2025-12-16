package com.example.genesis.service;

import com.example.genesis.exception.ResourceNotFoundException;
import com.example.genesis.exception.ValidationException;
import com.example.genesis.model.User;
import com.example.genesis.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PersonIdService personIdService;
    
    public UserService(UserRepository userRepository, PersonIdService personIdService) {
        this.userRepository = userRepository;
        this.personIdService = personIdService;
    }
    public User createUser(User user) {
        if (!personIdService.isPersonIdValid(user.getPersonId())) {
            throw new ValidationException("PersonID '" + user.getPersonId() + "' není autorizováno Genesis Authority.");
        }
        if (userRepository.findByPersonId(user.getPersonId()).isPresent()) {
            throw new ValidationException("PersonID '" + user.getPersonId() + "' je již zaregistrováno u jiného uživatele.");
        }
        user.setUuid(UUID.randomUUID().toString());
        
        return userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel s ID " + id + " nebyl nalezen."));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}