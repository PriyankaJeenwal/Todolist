package com.tasktracker.userservice.Service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.response.UserResponse;


@Service
public interface UserService {

	Object ERROR_MESSAGE = null;

	public UserResponse<User> createUser(User user);

	public UserResponse<List<User>> getAllUsers(Pageable paging);

	public UserResponse<List<User>> getUserById(Long id);

	public UserResponse<List<User>> getUsersBetweenDates(Date startDate, Date endDate);

	public UserResponse<List<User>> updateUser(Long id, User updateUser) throws Exception;

	public String deleteUser(Long id);

	public String uploadImage(String path, MultipartFile file) ;

	public void sendSimpleMessage(String email, String password);

	public Boolean checkOtp(String email, Long otp);

}
