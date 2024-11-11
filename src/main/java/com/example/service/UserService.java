package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.UserDto;
import com.example.model.entity.User;
import com.example.model.enums.Role;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return this.getByUsername(username);
            }
        }
        return null;
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
        user.setRole(userRequest.getRole());

        User updated = userRepository.save(user);
        return UserDto.toDto(updated);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}

