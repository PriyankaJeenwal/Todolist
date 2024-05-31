package com.tasktracker.userservice.ServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tasktracker.userservice.Entity.Task;
import com.tasktracker.userservice.Entity.TaskResponse;
import com.tasktracker.userservice.Entity.User;
import com.tasktracker.userservice.Repository.UserRepository;
import com.tasktracker.userservice.Service.UserService;
import com.tasktracker.userservice.feignclient.TaskClient;
import com.tasktracker.userservice.response.UserResponse;

@Service
public class UserSeviceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskClient taskClient;

	@Autowired
	JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(UserSeviceImpl.class);
	private static final String ERROR_MESSAGE = "Something went wrong";

	@Override
	public UserResponse<User> createUser(User user) {
		List<User> userList = new ArrayList<User>();

		try {
			if (!Objects.equals(user.getEmail(), "") && !Objects.equals(user.getName(), "")) {
				if (user.getEmail() != null && user.getName() != null) {
					Optional<User> existUser = userRepository.findByEmail(user.getEmail());

					if (existUser.isPresent()) {
						userList.add(existUser.get());
						return new UserResponse<>("User already exists", userList, false);
					} else {
						if (user.getPassword().matches("[a-zA-Z0-9@$]{8,}+")) {
							String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
						user.setPassword(encoded);
							userRepository.save(user);
							return new UserResponse<>("User saved successfully", userList, true);
						} else {
							return new UserResponse<>(
									"Password should contain uppercase, lowercase, digit, and special character and length must be 8 characters",
									userList, false);
						}
					}
				} else {
					return new UserResponse<>("User cannot be null", userList, false);
				}
			} else {
				return new UserResponse<>("User cannot be empty", userList, false);
			}
		} catch (Exception e) {
			return new UserResponse<>(ERROR_MESSAGE, userList, false);
		}
	}
	
	
	@Override
	public UserResponse<List<User>> getAllUsers(Pageable paging) {
		List<User> newUserList = new ArrayList<User>();
		try {
			Page<User> userPage = userRepository.findAll(paging);
			List<User> userList = userPage.getContent();


			for (User user : userList) {
				ResponseEntity<TaskResponse<List<Task>>> taskByUserId = taskClient.getTaskByUserId(user.getId());
				//System.out.println(taskByUserId);
				TaskResponse<List<Task>> body = taskByUserId.getBody();
				List<Task> taskList = body.getData();
				System.out.println(taskList);
				user.setTask(taskList);
				newUserList.add(user);

			}
			System.out.println(newUserList);
			return new UserResponse<List<User>>("Users fetched successfully", newUserList, true);
		} catch (RuntimeException e) {
			return new UserResponse<List<User>>(ERROR_MESSAGE, newUserList, false);
		}
	}

	@Override
	public UserResponse<List<User>> getUserById(Long id) {
		List<User> userList = new ArrayList<User>();
		try {
			Optional<User> getUser = userRepository.findById(id);
			if (getUser.isPresent()) {
				User user = getUser.get();
				user.setOtp(null);
				// fetch task by userId
				ResponseEntity<TaskResponse<List<Task>>> taskByUserId = taskClient
						.getTaskByUserId(getUser.get().getId());
				TaskResponse<List<Task>> body = taskByUserId.getBody();
				List<Task> taskList = body.getData();
				System.out.println(taskList);
				user.setTask(taskList);
				userList.add(user);

			}

			return new UserResponse<List<User>>("Users fetched successfully", userList, true);
		} catch (Exception e) {
			return new UserResponse<List<User>>(ERROR_MESSAGE, userList, false);
		}
	}

	@Override
	public UserResponse<List<User>> getUsersBetweenDates(Date startDate, Date endDate) {
		List<User> userList = new ArrayList<User>();
		try {
			userList = userRepository.findByCreationDateBetween(startDate, endDate).orElse(null);
			if (userList != null && !userList.isEmpty()) {
				return new UserResponse<>("Users fetched successfully", userList, true);
			} else {
				return new UserResponse<>("No users found between the given dates", userList, false);
			}
		} catch (Exception e) {
			return new UserResponse<>(ERROR_MESSAGE, userList, false);
		}
	}

	@Override
	public UserResponse<List<User>> updateUser(Long id, User updateUser) {

		List<User> userList = new ArrayList<User>();
		try {
			if (id == null || updateUser == null) {
				throw new IllegalArgumentException("User id and updated user cannot be null");
			}

			User user = userRepository.findById(id).orElse(null);
			if (user == null) {
				return (new UserResponse<>("User not found with id: " + id, userList, false));
			}

			if (updateUser.getName() != null && !updateUser.getName().isEmpty()) {
				user.setName(updateUser.getName());
			}
			if (updateUser.getEmail() != null && !updateUser.getEmail().isEmpty()) {
				user.setEmail(updateUser.getEmail());
			}
			userList.add(user);
			userRepository.save(user);
			return new UserResponse<>("User updated successfully", userList, false);
		} catch (Exception e) {
			return (new UserResponse<>(ERROR_MESSAGE, userList, false));
		}
	}

	@Override
	public String deleteUser(Long id) {
		logger.info("finding the id of the User for getUserById");
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			logger.error("User ID is  not present=" + id);
			return "user is not present";
		} else {
			userRepository.deleteById(id);
			logger.info("Uesr with ID " + id + " deleted successfully.");
			return "user deleted sucessfully";
		}
	}

	@Override
	public String uploadImage(String path, MultipartFile file) {
		// TODO Auto-generated method stub

		// File name

		String name = file.getOriginalFilename();
		// File Path

		String filePath = path + File.separator + name;

		// create folder if not created

		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy

		try {

			File files = new File("images/" + file.getOriginalFilename());
			if (files.exists()) {
				System.out.println("file already exist");
			} else {
				Files.copy(file.getInputStream(), Paths.get(filePath));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	@Override

	public void sendSimpleMessage(String email, String password) {
		try {
			Random random = new Random();
			long otp = random.nextInt(1000, 9999);
			Optional<User> user = userRepository.findByEmail(email);
			if (user.isPresent() && user.get().getPassword().equals(password)) {
				user.get().setOtp(otp);
				userRepository.save(user.get());
				String body = "your OTP is " + otp;
				String subject = "otp verification";
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("harshit.vyas@intelliatech.com");
				message.setTo(email);
				message.setSubject(subject);
				message.setText(body);
				javaMailSender.send(message);

			}

		} catch (Exception e) {
			logger.error("UserServiceImpl {}", e);
		}
	}

	@Override
	public Boolean checkOtp(String email, Long otp) {

		Optional<User> user = userRepository.findByEmail(email);
		if (user.get().getOtp().equals(otp)) {
			user.get().setOtp(null);
			userRepository.save(user.get());
			return true;
		}
		user.get().setOtp(null);
		userRepository.save(user.get());
		return false;

	}
	
	
	

}
