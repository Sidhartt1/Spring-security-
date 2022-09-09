package com.example.demo.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PasswordModel;
import com.example.demo.entity.PasswordVerifyToken;
import com.example.demo.entity.UserModel;
import com.example.demo.entity.Users;
import com.example.demo.entity.VerificationToken;
import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController

public class RegistrationController 
{
	@Autowired
	private UserService userv;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PasswordEncoder pe;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, HttpServletRequest request)
	{
		Users user = userv.saveInDb(userModel);
		publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
		return "success";
	}

	private String applicationUrl(HttpServletRequest request) 
	{
		return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}
	
	@GetMapping("/verify")
	public String verifyToken(@RequestParam("token") String token)
	{
		String result = userv.VerifyAndGetUser(token);
		if(result.equalsIgnoreCase("valid"))
		{
			return "user_verified";
		}
		return "Sorry. Try again later";
		
	}
	
	@GetMapping("/resend")
	public void resendVerifyEmail(@RequestParam("token") String token, HttpServletRequest request)
	{
		VerificationToken vtValid = userv.generateNewToken(token);
		userv.VerifyResendAndGetUser(vtValid.getToken(),applicationUrl(request));
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody PasswordModel pm,HttpServletRequest request)
	{
		Users user = userv.GetUserFromEmail(pm.getEmail());
		if(user!=null)
		{
			String token = UUID.randomUUID().toString();
			PasswordVerifyToken pt = new PasswordVerifyToken(user,token);
			userv.saveInDb(pt);
			userv.sendPassToken(user,applicationUrl(request),pt.getToken());
			return "LinkSend";
		}
		return "try again";
	}
	
	@PostMapping("/VerifyPasswordToken")
	public String changePassword(@RequestParam("ptoken") String ptoken,@RequestBody PasswordModel pm)
	{
		
		String tokenValid = userv.validatePasswordToken(ptoken);
		if(!tokenValid.equalsIgnoreCase("valid"))
		{
			return "invalid";
		}
		Users user = userv.getUserFromPtoken(ptoken);
		if(user!=null)
		{
			userv.changePassword(user,pm.getNewPassword());
			return "Pssword Changed";
		}
		return "Password not changed";
	}
	
	@PostMapping("/ChangeUserPassword")
	public String changeUserPassword(@RequestBody PasswordModel pm )
	{
		String userValid = userv.findUserAndValidate(pm.getEmail(),pm.getOldPassword());
		if(userValid.equalsIgnoreCase("valid"))
		{
			Users user = userv.getUserByEmail(pm.getEmail());
			user.setPassword(pe.encode(pm.getNewPassword()));
			return "Password Changed";
		}
		return "Password Not Changed";
		
	}
}