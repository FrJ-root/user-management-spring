package com.userms.service.impl;

import java.util.List;
import com.userms.dto.*;
import com.userms.entity.User;
import java.util.stream.Collectors;
import com.userms.mapper.UserMapper;
import com.userms.service.UserService;
import org.springframework.data.domain.*;
import com.userms.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.userms.exception.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        User user = UserMapper.toEntity(dto);
        user = userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserResponseDTO> listUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new RuntimeException("Email already exists!"); });

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(User.Role.valueOf(dto.getRole().toUpperCase()));
        return UserMapper.toDTO(userRepository.save(user));
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("You Can not Delete Your Self, You are the ADMIN Of EVERYTHING ^_- ");
        }

        user.setActive(false);
        userRepository.save(user);
    }

}
