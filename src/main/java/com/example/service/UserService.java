package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.UserDto;
import com.example.model.entity.User;
import com.example.model.enums.Role;
import com.example.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Validator validator;
    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return UserDto.toDto(user);
        }
        throw new RuntimeException("User not authenticated");
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(User.class));

        return UserDto.toDto(user);
    }

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return UserDto.toDtoList(userList);
    }

    public UserDto findByRole(Role role) {
        User userByRole = userRepository.findByRole(role).orElse(null);
        return userByRole != null ? UserDto.toDto(userByRole) : null;
    }

    public UserDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(User.class));

        return UserDto.toDto(user);
    }

    public UserDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(User.class));

        return UserDto.toDto(user);
    }

    public UserDto update(Long id, UserDto userRequest) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(User.class));

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(Role.valueOf(userRequest.getRole()));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        User updated = userRepository.save(user);
        return UserDto.toDto(updated);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

