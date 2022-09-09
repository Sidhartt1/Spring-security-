package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

import com.example.demo.entity.Users;

import lombok.Data;


@Data
public class RegistrationCompleteEvent extends ApplicationEvent
{
	private Users user;
	
	private String applicationUrl;
	
	public RegistrationCompleteEvent(Users user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}
	
}