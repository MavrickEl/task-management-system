package com.example.userservice.service.Impl;

import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.exception.UserException;
import com.example.userservice.provider.UserFieldsRequestDtoProvider;
import com.example.userservice.provider.UserProvider;
import com.example.userservice.provider.UserRequestDtoProvider;
import com.example.userservice.provider.UserResponseDtoProvider;
import com.example.userservice.repository.UserRepo;
import com.example.userservice.service.impl.UserServiceImpl;
import com.example.userservice.util.PasswordUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepo userRepository;

    @BeforeAll
    static void init() {
        Mockito.mockStatic(PasswordUtil.class);
    }

    @Test
    void getById_whenUserExists_shouldReturnUserResponseDto() {
        var user = UserProvider.getUser();
        var userResponse = UserResponseDtoProvider.getUserResponse();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getById(anyLong());

        assertEquals(userResponse, result);
    }

    @Test
    void getById_whenUserNotExists_AndThrowError() {
        when(userRepository.findById(anyLong())).thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.getById(1L));
    }

    @Test
    void getByEmail_whenUserExists_shouldReturnUserResponseDto() {
        var user = UserProvider.getUser();
        var userResponse = UserResponseDtoProvider.getUserResponse();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getByEmail(anyString());

        assertEquals(userResponse, result);
    }

    @Test
    void getByEmail_whenUserNotExists_AndThrowError() {
        var user = UserProvider.getUser();
        when(userRepository.findByEmail(anyString())).thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.getByEmail(user.getEmail()));
    }

    @Test
    void getAll_whenUsersExist_shouldReturnListOfUserResponseDto() {
        var user = UserProvider.getUser();
        var userResponse = UserResponseDtoProvider.getUserResponse();
        when(userRepository.findAll()).thenReturn(List.of(user, user));

        List<UserResponseDto> result = userService.getAll();

        assertEquals(Arrays.asList(userResponse, userResponse), result);
    }

    @Test
    void getAll_whenUsersNotExist_shouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDto> result = userService.getAll();

        assertEquals(List.of(), result);
    }

    @Test
    void save_whenUserJonesNotExistByEmail_shouldReturnUserResponseDto() {
        var user = UserProvider.getUser();
        var saveUser = UserProvider.getUserForSave();
        var saveUserRequest = UserRequestDtoProvider.getSaveUserRequest();
        var userResponse = UserResponseDtoProvider.getUserResponse();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(saveUser)).thenReturn(user);

        UserResponseDto result = userService.save(saveUserRequest);

        assertEquals(userResponse, result);
    }

    @Test
    void save_whenUserJonesExistByEmail_AndThrowError() {
        var saveUserRequest = UserRequestDtoProvider.getSaveUserRequest();
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.save(saveUserRequest));
    }

    @Test
    void update_whenUserExists_shouldReturnUserResponseDto() {
        var user = UserProvider.getUser();
        var updateUser = UserProvider.getUpdateUserWithId();
        var updateUserRequest = UserRequestDtoProvider.getUpdateUserRequest();
        var updateUserResponse = UserResponseDtoProvider.getUpdateUserResponse();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(PasswordUtil.hashPassword(anyString())).thenReturn(updateUser.getPassword());
        when(userRepository.save(updateUser)).thenReturn(updateUser);

        UserResponseDto result = userService.update(anyLong(), updateUserRequest);

        assertEquals(updateUserResponse, result);
    }

    @Test
    void update_whenUserExists_AndEmailAlreadyExist_AndThrowError() {
        var user = UserProvider.getUser();
        var updateUserRequest = UserRequestDtoProvider.getUpdateUserRequest();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.update(1L, updateUserRequest));
    }

    @Test
    void update_whenUserNotExists_shouldReturnUserResponseDto() {
        var updateUser = UserProvider.getUpdateUserWithoutId();
        var updatedUser = UserProvider.getUpdateUserWithId();
        var updateUserRequest = UserRequestDtoProvider.getUpdateUserRequest();
        var updateUserResponse = UserResponseDtoProvider.getUpdateUserResponse();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(PasswordUtil.hashPassword(anyString())).thenReturn(updateUser.getPassword());
        when(userRepository.save(updateUser)).thenReturn(updatedUser);

        UserResponseDto result = userService.update(anyLong(), updateUserRequest);

        assertEquals(updateUserResponse, result);
    }

    @Test
    void update_whenUserNotExists_AndEmailAlreadyExist_AndThrowError() {
        var updateUserRequest = UserRequestDtoProvider.getUpdateUserRequest();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.update(1L, updateUserRequest));
    }

    @Test
    void updateFields_whenUserExists_shouldReturnUserResponseDto() throws IllegalAccessException {
        var user = UserProvider.getUser();
        var userResponseDto = UserResponseDtoProvider.getUpdateFieldsUserResponse();
        var updateUserFieldsRequest = UserFieldsRequestDtoProvider.getUpdateUserFieldsRequest();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto result = userService.updateFields(anyLong(), updateUserFieldsRequest);

        assertEquals(userResponseDto, result);
    }

    @Test
    void updateFields_whenUserNotExists_AndThrowError() {
        var updateUserFieldsRequest = UserFieldsRequestDtoProvider.getUpdateUserFieldsRequest();
        when(userRepository.findById(anyLong())).thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.updateFields(1L, updateUserFieldsRequest));
    }

    @Test
    void deleteById_whenUserExists_shouldDeleteUser() {
        var user = UserProvider.getUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteById(anyLong());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteById_whenUserNotExists_AndThrowError() {
        when(userRepository.findById(anyLong()))
                .thenThrow(new UserException(anyString()));

        assertThrows(UserException.class, () -> userService.deleteById(1L));
    }

}