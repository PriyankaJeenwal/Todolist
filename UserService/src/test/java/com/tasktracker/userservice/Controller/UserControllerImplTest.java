package com.tasktracker.userservice.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.UserServiceApplication;
import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.response.UserResponse;


@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UserControllerImplTest extends UserControllerImpl {

	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserControllerImpl  UserControllerImpl ;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //call it while deleting and updating and any other
	User user;
	
	
	
	
	 @Test
	    public void testCreateUserSuccess() {
	        // Given
	        User newUser = new User();
	        newUser.setName("Test");
	        newUser.setEmail("test@example.com");

	        UserResponse<User> userResponse = new UserResponse<>();
	        userResponse.setData(newUser);
	        userResponse.setStatus(true);

	        // When
	        when(userService.createUser(newUser)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<User>> response = UserControllerImpl.createUser(newUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        assertEquals(newUser, response.getBody().getData());
	    }

	    @Test
	    public void testCreateUserNotFound() {
	        // Given
	        User newUser = new User();
	        newUser.setName("Test");
	        newUser.setEmail("test@example.com");

	        UserResponse<User> userResponse = new UserResponse<>();
	        userResponse.setStatus(false);

	        // When
	        when(userService.createUser(newUser)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<User>> response = UserControllerImpl.createUser(newUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    public void testCreateUserException() {
	        // Given
	        User newUser = new User();
	        newUser.setName("Test");
	        newUser.setEmail("test@example.com");

	        // When
	        when(userService.createUser(newUser)).thenThrow(new RuntimeException());

	        // Act
	        ResponseEntity<UserResponse<User>> response = UserControllerImpl.createUser(newUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }
	@Test
	@Order(1)
	public void testGetAllUsersSuccess() {
        // Given
        int pageSize = 5;
        int page = 0;
        Pageable paging =  PageRequest.of(page, pageSize);

        UserResponse<List<User>> userResponse = new UserResponse<>();
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"abc","abc@1234","Abc@123")); // Add some mock users
        userResponse.setData(userList);
        userResponse.setStatus(true);

        // When
        when(userService.getAllUsers(paging)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getAllUsers(pageSize, page);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userList, response.getBody().getData());
    }

	 @Test
	    public void testGetAllUsersNotFound() {
	        // Given
	        int pageSize = 5;
	        int page = 0;
	        Pageable paging = PageRequest.of(page, pageSize);

	        UserResponse<List<User>> userResponse = new UserResponse<>();
	        userResponse.setStatus(false);

	        // When
	        when(userService.getAllUsers(paging)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getAllUsers(pageSize, page);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    public void testGetAllUsersException() {
	        // Given
	        int pageSize = 5;
	        int page = 0;
	        Pageable paging = PageRequest.of(page, pageSize);

	        // When
	        when(userService.getAllUsers(paging)).thenThrow(new RuntimeException());

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getAllUsers(pageSize, page);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }
	    
//	    @Test
//	    public void getUserById() {
//	    	User user=new User(2,"abc","abc@1234","Abc@123");
//	    	Long id=2L;
//	    	
//	        when(userService.getUserById(id)).thenReturn(userResponse);
//	    }
	    
	    
	    @Test
	    public void testGetUserByIdSuccess() {
	        // Given
	        Long userId = 1L;
	        UserResponse<List<User>> userResponse = new UserResponse<>();
	        List<User> userList = new ArrayList<>();
	        
	        User user = new User();
	        user.setId(userId);
	        user.setName("Test");
	        user.setEmail("Test@example.com");
	        
	        userList.add(user);
	        
	        userResponse.setData(userList);
	        userResponse.setStatus(true);

	        // When
	        when(userService.getUserById(userId)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getUserById(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        assertEquals(userList, response.getBody().getData());
	    }
	    
	    @Test
	    public void testGetUserByIdNotFound() {
	        // Given
	        Long userId = 1L;
	        UserResponse<List<User>> userResponse = new UserResponse<>();
	        userResponse.setStatus(false);

	        // When
	        when(userService.getUserById(userId)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getUserById(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    public void testGetUserByIdException() {
	        // Given
	        Long userId = 1L;

	        // When
	        when(userService.getUserById(userId)).thenThrow(new RuntimeException());

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.getUserById(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }
	    
	    

	    @Test
	    public void testUpdateUserSuccess() throws Exception {
	        // Given
	        Long userId = 1L;
	        User updateUser = new User();
	        updateUser.setName("Updated Name");
	        updateUser.setEmail("updated.email@example.com");

	        UserResponse<List<User>> userResponse = new UserResponse<>();
	        List<User> userList = new ArrayList<>();
	        userList.add(updateUser);
	        
	        userResponse.setData(userList);
	        userResponse.setStatus(true);

	        // When
	        when(userService.updateUser(userId, updateUser)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(userList, response.getBody().getData());
	    }

	    @Test
	    public void testUpdateUserNotFound() throws Exception {
	        // Given
	        Long userId = 1L;
	        User updateUser = new User();

	        UserResponse<List<User>> userResponse = new UserResponse<>();
	        userResponse.setStatus(false);

	        // When
	        when(userService.updateUser(userId, updateUser)).thenReturn(userResponse);

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    public void testUpdateUserException() throws Exception {
	        // Given
	        Long userId = 1L;
	        User updateUser = new User();

	        // When
	        when(userService.updateUser(userId, updateUser)).thenThrow(new RuntimeException());

	        // Act
	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    public void testDeleteUserSuccess() {
	        // Given
	        Long userId = 1L;
	        String expectedResponse = "User deleted successfully";

	        // When
	        when(userService.deleteUser(userId)).thenReturn(expectedResponse);

	        // Act
	        ResponseEntity<String> response = UserControllerImpl.deleteUser(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(expectedResponse, response.getBody());
	    }

	    @Test
	    public void testDeleteUserInvalidId() {
	        // Given
	        Long userId = 0L;

	        // Act
	        ResponseEntity<String> response = UserControllerImpl.deleteUser(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	        assertEquals("user not deleted", response.getBody());
	    }

	    @Test
	    public void testDeleteUserException() {
	        // Given
	        Long userId = 1L;

	        // When
	        doThrow(new RuntimeException()).when(userService).deleteUser(userId);

	        // Act
	        ResponseEntity<String> response = UserControllerImpl.deleteUser(userId);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	    }
	    @Test
	    public void testFileUploadSuccess() {
	        // Given
	        MultipartFile image = mock(MultipartFile.class);

	        // When
	        ResponseEntity<User> response = UserControllerImpl.fileUpload(image);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	    }


	    @Test
	    public void testSendEmailSuccess() {
	        // Given
	        String email = "test@example.com";
	        String password = "test123";

	        // When
	        ResponseEntity<String> response = UserControllerImpl.sendEmail(email, password);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("mail send", response.getBody());
	    }

	    @Test
	    public void testSendEmailException() {
	        // Given
	        String email = "test@example.com";
	        String password = "test123";
	        doThrow(new RuntimeException()).when(userService).sendSimpleMessage(email, password);

	        // When
	        ResponseEntity<String> response = UserControllerImpl.sendEmail(email, password);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals("mail could not send", response.getBody());
	    }

	    @Test
	    public void testVerifyOtpSuccess() {
	        // Given
	        String email = "test@example.com";
	        Long otp = 123456L;
	        when(userService.checkOtp(email, otp)).thenReturn(true);

	        // When
	        ResponseEntity<String> response = UserControllerImpl.verifyOtp(email, otp);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals("otp matched ", response.getBody());
	    }

	    @Test
	    public void testVerifyOtpInvalidOtp() {
	        // Given
	        String email = "test@example.com";
	        Long otp = 123456L;
	        when(userService.checkOtp(email, otp)).thenReturn(false);

	        // When
	        ResponseEntity<String> response = UserControllerImpl.verifyOtp(email, otp);

	        // Then
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals("invalid otp ", response.getBody());
	    }
	    

	    
	    @Test
	    void testUpdateUser_Success() throws Exception {
	        Long userId = 1L;
	        User updateUser = new User();
	        List<User> users = new ArrayList<>();
	        users.add(updateUser);
	        UserResponse<List<User>> userResponse = new UserResponse<>("User updated successfully", users, true);

	        when(userService.updateUser(userId, updateUser)).thenReturn(userResponse);

	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        verify(userService, times(1)).updateUser(userId, updateUser);
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(userResponse, response.getBody());
	    }

	    @Test
	    void testUpdateUser_NotFound() throws Exception {
	        Long userId = 1L;
	        User updateUser = new User();
	        UserResponse<List<User>> userResponse = new UserResponse<>("User not found", new ArrayList<>(), false);

	        when(userService.updateUser(userId, updateUser)).thenReturn(userResponse);

	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        verify(userService, times(1)).updateUser(userId, updateUser);
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

	    @Test
	    void testUpdateUser_Exception() throws Exception {
	        Long userId = 1L;
	        User updateUser = new User();

	        when(userService.updateUser(userId, updateUser)).thenThrow(new RuntimeException("Exception occurred"));

	        ResponseEntity<UserResponse<List<User>>> response = UserControllerImpl.updateUser(userId, updateUser);

	        verify(userService, times(1)).updateUser(userId, updateUser);
	        assertNotNull(response);
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    }

}
