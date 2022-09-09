package com.example.demo.Listener;

import java.util.UUID;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.addressing.messageid.UuidMessageIdStrategy;

import com.example.demo.entity.Users;
import com.example.demo.entity.VerificationToken;
import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.repository.VerificationRepo;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>
{
	@Autowired
	private UserService userv;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) 
	{
		Users user = event.getUser();
		String token = UUID.randomUUID().toString();
		userv.saveVerifcationToken(token);
		String url = event.getApplicationUrl()+"/verify?token="+token;
		log.info("click this link to register : {}",url);
	}
	
	
}