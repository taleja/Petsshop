package com.petsshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.petsshop.service.JmsClientService;

@RequestMapping("/message")
@Controller 
public class MessageBrockerController {

	@Autowired
	private JmsClientService jmsClientService;
	
	@RequestMapping("/receive")
	@ResponseBody 
	public String receive() {
		System.out.println("Received meessage..."); 
		return jmsClientService.receive();
	}
	
	@RequestMapping("/send")
	@ResponseBody
	public String send(@RequestParam String message) {
		System.out.println("Sent message...");
		jmsClientService.send(message);
		return "Sent";
	}
}
