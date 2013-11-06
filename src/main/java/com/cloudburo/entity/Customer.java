package com.cloudburo.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

import org.joda.time.LocalDateTime;

@Entity
public class Customer {
	@Id Long _id;
	String name;
	String surname;
	String email;
	String address;
	String plz;
	String location;
	Date date;
	LocalDateTime date1;
	
	public void setDate(Date aDate) {
		date =aDate;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate1(LocalDateTime aDate) {
		date1 =aDate;
	}
	
	public LocalDateTime getDate1() {
		return date1;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	String telephone;
	String birthdate;
	String citizenship;
	String mobile;
	
	public void set_id(Long id) {
		_id = id;
	}
	
	public Long get_id() {
		return _id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public String getAddress() {
		return address;
	}
	public String getPlz() {
		return plz;
	}
	public String getLocation() {
		return location;
	}
	public String getTelephone() {
		return telephone;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public String getCitizenship() {
		return citizenship;
	}
	public String getMobile() {
		return mobile;
	}
	
	
	
}
