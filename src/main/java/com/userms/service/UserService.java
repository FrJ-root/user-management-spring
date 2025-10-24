package com.userms.service;

import com.userms.dto.UserRequestDTO;
import com.userms.dto.UserResponseDTO;
import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    List<UserResponseDTO> listUsers(int page, int size);

    UserResponseDTO getUser(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);
}
