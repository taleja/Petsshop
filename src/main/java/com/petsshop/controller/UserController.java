package com.petsshop.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petsshop.dto.UserDTO;
import com.petsshop.dto.UserDetailsDTO;
import com.petsshop.model.User;
import com.petsshop.service.UserService;
import com.petsshop.util.UserNotFoundException;

@Controller
@RequestMapping("/login")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RestController restController;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<UserDetailsDTO> post(@RequestBody UserDTO userDto) {
		User user = null;
		try {
			user = userService.getUser(userDto);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
		}
		UserDetailsDTO userDetais = new UserDetailsDTO(); 

		userDetais.setSessionId(user.getId());
		userDetais.setRoleString(user.getRole());
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/petsshop/petsshop/rest/products"));
		
		return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
	}
}
