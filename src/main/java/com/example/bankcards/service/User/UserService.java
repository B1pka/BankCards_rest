package com.example.bankcards.service.User;

import com.example.bankcards.dto.User.CreateUserRequest;
import com.example.bankcards.dto.User.UserResponse;
import com.example.bankcards.entity.User.User;
import com.example.bankcards.repository.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request){
        if(userRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        User user = new User(request.username(), passwordEncoder.encode(request.password()), request.role());
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с такми id не найден"));
        return mapToResponse(user);
    }

    public User getUserEntityByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким именем не найден"));
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

}
