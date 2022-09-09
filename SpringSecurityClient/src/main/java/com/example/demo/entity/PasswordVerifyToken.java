package com.example.demo.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PasswordVerifyToken 
{
	private static final int EXPIRATION_TIME = 10;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String token;
	
	private Date expirationTime;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id",nullable = false, foreignKey = @ForeignKey(name="PT_USER_VERIFY_TOKEN"))
	private Users user;
	
	public PasswordVerifyToken(Users user, String token)
	{
		super();
		this.user = user;
		this.token = token;
		this.expirationTime = calculateExpiry(EXPIRATION_TIME);
	}
	
	public PasswordVerifyToken(String token)
	{
		super();
		this.token = token;
		this.expirationTime = calculateExpiry(EXPIRATION_TIME);
	}

	private Date calculateExpiry(int expirationTime2) 
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expirationTime2);
		return new Date(cal.getTime().getTime());
	}
}