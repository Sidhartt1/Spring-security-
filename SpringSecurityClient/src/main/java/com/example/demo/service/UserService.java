package com.example.demo.service;

import com.example.demo.entity.PasswordVerifyToken;
import com.example.demo.entity.UserModel;
import com.example.demo.entity.Users;
import com.example.demo.entity.VerificationToken;

public interface UserService 
{

	public Users saveInDb(UserModel userModel);

	public String VerifyAndGetUser(String token);

	public void saveVerifcationToken(String token);


	public VerificationToken generateNewToken(String token);

	public void resendVerificationToken(Users user, String token, String string);

	public String VerifyResendAndGetUser(String token, String applicationUrl);

	public Users GetUserFromEmail(String email);

	public void VerifyUserAndGenerateToken(Users user);

	public void saveInDb(PasswordVerifyToken pt);

	public void sendPassToken(Users user, String applicationUrl, String string);

	public String validatePasswordToken(String token);

	public Users getUserFromPtoken(String token);

	public void changePassword(Users user, String newPassword);

	

	public String findUserAndValidate(String email, String string);

	public Users getUserByEmail(String email);
	
}