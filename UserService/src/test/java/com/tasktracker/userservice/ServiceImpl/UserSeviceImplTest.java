
package com.tasktracker.userservice.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.Task;
import com.tasktracker.userservice.Entity.TaskResponse;
import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Repository.UserRepository;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.feignclient.TaskClient;
import com.tasktracker.userservice.response.UserResponse;

public class UserSeviceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskClient taskClient;

    @Mock
    private JavaMailSender javaMailSender;
    
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserSeviceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("Password@123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse<User> response = userServiceImpl.createUser(user);

        assertEquals("User saved successfully", response.getMessage());
        assertEquals(true, response.isSuccess());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        //when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        UserResponse<User> response = userServiceImpl.createUser(user);

        assertEquals("User already exists", response.getMessage());
        assertEquals(false, response.isSuccess());
    }
    
    @Test
    public void testCreateUser_InvalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("short");

        UserResponse<User> response = userServiceImpl.createUser(user);

        assertEquals("Password should contain uppercase, lowercase, digit, and special character and length must be 8 characters", response.getMessage());
        assertEquals(false, response.isSuccess());
    }

    
    @Test
    @Disabled
    public void testCreateUser_NullOrEmptyUser() {
        User user = new User();
        user.setEmail("");
        user.setName("");

        UserResponse<User> response = userService.createUser(user);

        assertFalse(response.isSuccess());
        assertEquals("User cannot be empty", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    public void testGetAllUsers_Success() {
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId((int) 1L);

        List<User> users = Arrays.asList(user);
        Page<User> userPage = new PageImpl<>(users);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
        taskResponse.setData(Arrays.asList(task));
        when(taskClient.getTaskByUserId(1L)).thenReturn(ResponseEntity.ok(taskResponse));

        UserResponse<List<User>> response = userServiceImpl.getAllUsers(PageRequest.of(0, 10));

        assertEquals("Users fetched successfully", response.getMessage());
        assertEquals(true, response.isSuccess());
        assertEquals(1, ((List<User>) response.getData()).size());
        assertEquals(1, ((List<User>) response.getData()).get(0).getTask().size());
    }
    
   
    

 // Test case for getUserById method
    @Test
    public void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findById(1L)).thenReturn(optionalUser);

        TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
        taskResponse.setData(new ArrayList<>());
        ResponseEntity<TaskResponse<List<Task>>> responseEntity = ResponseEntity.ok(taskResponse);
        when(taskClient.getTaskByUserId(anyLong())).thenReturn(responseEntity);

        UserResponse<List<User>> response = userServiceImpl.getUserById(1L);

        assertTrue(response.isSuccess());
        assertEquals("Users fetched successfully", response.getMessage());
    }


    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserResponse<List<User>> response = userServiceImpl.getUserById(1L);

        assertNotNull(response.getData()); // Ensure that response data is not null
        assertTrue(((List<User>) response.getData()).isEmpty());
        assertTrue(response.isSuccess());
    }


    
    @Test
    public void testGetUserById_Exception() {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Exception"));

        UserResponse<List<User>> response = userServiceImpl.getUserById(1L);

        assertFalse(response.isSuccess());
        assertEquals("Something went wrong", response.getMessage());
    }

    
    @Test
    public void testGetUsersBetweenDates_Success() {
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);

        when(userRepository.findByCreationDateBetween(any(Date.class), any(Date.class))).thenReturn(Optional.of(users));

        UserResponse<List<User>> response = userServiceImpl.getUsersBetweenDates(Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));

        assertTrue(response.isSuccess());
        assertEquals("Users fetched successfully", response.getMessage());
    }

    
    @Test
    public void testGetUsersBetweenDates_Exception() {
        when(userRepository.findByCreationDateBetween(any(Date.class), any(Date.class))).thenThrow(new RuntimeException("Exception"));

        UserResponse<List<User>> response = userServiceImpl.getUsersBetweenDates(Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));

        assertFalse(response.isSuccess());
        assertEquals("Something went wrong", response.getMessage());
    }

    
    @Test
    public void testGetUsersBetweenDates_NoUsersFound() {
        when(userRepository.findByCreationDateBetween(any(Date.class), any(Date.class))).thenReturn(Optional.of(new ArrayList<>()));

        UserResponse<List<User>> response = userServiceImpl.getUsersBetweenDates(Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));

        assertFalse(response.isSuccess());
        assertEquals("No users found between the given dates", response.getMessage());
    }


 // Test case for updateUser method
    @Test
    public void testUpdateUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Old Name");

        User updateUser = new User();
        updateUser.setName("New Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updateUser); // Return the updated user

        UserResponse<List<User>> response = userServiceImpl.updateUser(1L, updateUser);

        assertFalse(response.isSuccess());
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("New Name", user.getName()); // Verify that the user's name is updated
        verify(userRepository, times(1)).save(any(User.class)); // Verify that save method is called once
    }



    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserResponse<List<User>> response = userServiceImpl.updateUser(1L, new User());

        assertFalse(response.isSuccess());
        assertEquals("User not found with id: 1", response.getMessage());
    }



    @Test
    public void testDeleteUser_Success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String response = userServiceImpl.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
        assertEquals("user deleted sucessfully", response);
        
    }



    @Test
    public void testSendSimpleMessage_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("Password@123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userServiceImpl.sendSimpleMessage("test@example.com", "Password@123");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testCheckOtp_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setOtp(1234L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Boolean result = userServiceImpl.checkOtp("test@example.com", 1234L);

        assertEquals(true, result);
        assertEquals(null, user.getOtp());
    }
    
    
    @Test
    public void testCheckOtp_Failure() {
        String email = "test@example.com";
        Long otp = 1234L;
        User user = new User();
        user.setEmail(email);
        user.setOtp(5678L); // Set a different OTP than the one being checked

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Boolean response = userServiceImpl.checkOtp(email, otp);

        assertFalse(response); // OTP check should fail
        assertNull(user.getOtp()); // Ensure that the user's OTP is cleared
        verify(userRepository, times(1)).save(user); // Verify that the user is saved after OTP check
    }

}