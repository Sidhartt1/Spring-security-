package com.example.demo.service;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PasswordVerifyToken;
import com.example.demo.entity.UserModel;
import com.example.demo.entity.Users;
import com.example.demo.entity.VerificationToken;
import com.example.demo.repository.PasswordRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.VerificationRepo;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserServiceImp implements UserService
{
	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private VerificationRepo vrepo;
	
	@Autowired
	private PasswordEncoder pe;
	
	@Autowired
	private PasswordRepo prepo;

	@Override
	public Users saveInDb(UserModel userModel) 
	{
		Users user = new Users();
		user.setEmail(userModel.getEmail());
		user.setFname(userModel.getFname());
		user.setLname(userModel.getLname());
		user.setPassword(pe.encode(userModel.getPassword()));
		user.setRole("USER");
		urepo.save(user);
		return user;
	}

	@Override
	public String VerifyAndGetUser(String token) 
	{
		VerificationToken vt = vrepo.findByToken(token);
		Users user = vt.getUser();
		if((user==null))
		{
			return "invalid Request";
		}
		Calendar cal = Calendar.getInstance();
		if((vt.getExpirationTime().getTime())-(cal.getTime().getTime())<=0)
		{
			return "expired";
		}
		user.setEnabled(true);
		urepo.save(user);
		return "valid";
		
	}

	@Override
	public void saveVerifcationToken(String token) 
	{
		VerificationToken vt = new VerificationToken(token);
		vrepo.save(vt);
	}

	@Override
	public VerificationToken generateNewToken(String token) 
	{
		VerificationToken vt =vrepo.findByToken(token);
		vt.setToken(UUID.randomUUID().toString());
		vrepo.save(vt);
		return vt;	
	}

	@Override
	public void resendVerificationToken(Users user, String token, String url) 
	{
		String appUrl = url+"/verify?token="+token;
		log.info("click this link {}",appUrl);
	}

	@Override
	public String VerifyResendAndGetUser(String token, String applicationUrl)
	{
		VerificationToken vt = vrepo.findByToken(token);
		Users user = vt.getUser();
		if(user==null)
		{
			return "invalid";
		}
		Calendar cal = Calendar.getInstance();
		if((vt.getExpirationTime().getTime()-cal.getTime().getTime())<=0)
		{
			return "expired";
		}
		user.setEnabled(true);
		urepo.save(user);
		String url = applicationUrl+"/verify?token="+vt.getToken();
		log.info("{}",url);
		return "valid";
	}

	@Override
	public Users GetUserFromEmail(String email) 
	{
		Users user = urepo.findByEmail(email);
		return user;
	}

	@Override
	public void VerifyUserAndGenerateToken(Users user) 
	{
		
	}

	@Override
	public void saveInDb(PasswordVerifyToken pt) 
	{
		prepo.save(pt);
	}

	@Override
	public void sendPassToken(Users user, String applicationUrl, String token) 
	{
		String url = applicationUrl+"/VerifyPasswordToken?ptoken="+token;
		log.info("click here to reset Password: {}",url);
	}

	@Override
	public String validatePasswordToken(String token) 
	{
		PasswordVerifyToken pt = prepo.findByToken(token);
		if(pt==null)
		{
			return "invalid";
		}
		Calendar cal = Calendar.getInstance();
		if((pt.getExpirationTime().getTime()-cal.getTime().getTime())<=0)
		{
			return "expired";
		}
		return "valid";
	}

	@Override
	public Users getUserFromPtoken(String token) 
	{
		PasswordVerifyToken pt = prepo.findByToken(token);
		Users user = pt.getUser();
		return user;
	}

	@Override
	public void changePassword(Users user, String newPassword) 
	{
		user.setPassword(newPassword);
		urepo.save(user);
	}

	@Override
	public String findUserAndValidate(String email,String OldPassword) 
	{
		Users user = urepo.findByEmail(email);
		if(!(user==null))
		{
			return "valid";
		}
		if(pe.matches(user.getPassword(),OldPassword))
		{
			return "valid";
		}
		return "invalid";
	}

	@Override
	public Users getUserByEmail(String email) 
	{
		Users user = urepo.findByEmail(email);
		return user;
	}
	
}