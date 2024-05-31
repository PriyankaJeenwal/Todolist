package com.tasktracker.userservice.response;

import java.util.function.BooleanSupplier;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserResponse<T> {
	private String message;
	private Object data;
	private boolean Status;

	public UserResponse(String message,Object data, boolean status) {
		super();
		this.message = message;
		this.data = data;
		Status = status;
		
		
	}
public boolean isSuccess()
{
return Status;	
}
	

	
}
