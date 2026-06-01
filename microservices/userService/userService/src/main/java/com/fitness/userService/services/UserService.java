package com.fitness.userService.services;

import com.fitness.userService.dto.RegisterRequest;
import com.fitness.userService.dto.UserResponse;
import com.fitness.userService.models.User;
import com.fitness.userService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("User with this email already exists");
        }

        // Implement registration logic
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In a real application, make sure to hash the password!



        User savedUser = userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setName(savedUser.getName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }
}
