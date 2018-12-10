package com.petsshop.dto;

public class UserDetailsDTO {

	private Long sessionId;
	private String role;
	private UserDTO userInfo;

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public UserDTO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserDTO userInfo) {
		this.userInfo = userInfo;
	}

	public String getRole() {
		return role;
	}
	
	public void setRoleString(String role) {
		this.role = role;
	}
}
