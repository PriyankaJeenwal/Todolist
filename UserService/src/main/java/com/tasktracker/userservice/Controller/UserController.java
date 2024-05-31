package com.tasktracker.userservice.Controller;

import java.sql.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.response.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Service", description = "These service perform all crud operation of User with some extra features")
public interface UserController {

	@Operation(summary = "api to create User", description = "this api create user and save it in database")
	@ApiResponse(responseCode = "201", description = "HTTP status code 200 Created")
	@PostMapping("/createUser")
	public ResponseEntity<UserResponse<User>> createUser(@RequestBody User user);

	@Operation(summary = "api to get all User", description = "This api fetch all users from database")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@GetMapping("/getUsers")
	public ResponseEntity<UserResponse<List<User>>> getAllUsers(
			@RequestParam(defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page);

	
	@Operation(summary = "api to get User by Id", description = "this api get user by id from database")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@GetMapping("/getUsersById")
	public ResponseEntity<UserResponse<List<User>>> getUserById(@RequestParam Long id);

	@Operation(summary = "api to get User By Date", description = "this api get user by date from database")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@GetMapping("/getUserByDate")
	public ResponseEntity<UserResponse<List<User>>> getUsersBetweenDates(@RequestParam Date startDate,
			@RequestParam Date endDate);

	@Operation(summary = "api to update User", description = "this api update user and save it in database")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@PostMapping("/updateUser")
	public ResponseEntity<UserResponse<List<User>>> updateUser(@RequestParam Long id, @RequestBody User updateUser)
			throws Exception;

	@Operation(summary = "api to delete User", description = "this api delete user save in the database")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@PostMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam Long id);

	@Operation(summary = "api to upload file", description = "This api upload file")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@PostMapping("/upload")
	public ResponseEntity<User> fileUpload(@RequestParam("image") MultipartFile image);

	@Operation(summary = "api to login User", description = "This api login User")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@GetMapping("/login")
	public ResponseEntity<String> sendEmail(@RequestParam("email") String email,
			@RequestParam("password") String password);

	@Operation(summary = "api to verify otp")
	@ApiResponse(responseCode = "200", description = "HTTP status code 200 OK")
	@GetMapping("/verifyOtp")
	public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") Long otp);
}