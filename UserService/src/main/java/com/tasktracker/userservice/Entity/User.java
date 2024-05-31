package com.tasktracker.userservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity


@Schema(name="UserService")
public class User {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@Schema(name="userId")
	private Long id;

	@Schema(name="userName")
	@Column(name = "name")
	private String name;

	@Schema(name="userEmail")
	@Column(name = "email")
	private String email;

	@Schema(name="userPassword")
	@Column(name = "password")
	private String password;

	@Column(name = "otp")
	private Long otp;

	@Column(name = "creation_date")
	@CreationTimestamp
	private Date creationDate;

	@Column(name = "fileName")
	private String fileName;

	@Transient
	private List<Task> task;
	
	public User(int i, String string, String string2, String string3) {
	}

}
