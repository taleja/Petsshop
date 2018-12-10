package com.petsshop.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Value("TestDestination")
	String deatinationName;
	
	public void sendMessage(String message) {
		jmsTemplate.convertAndSend(deatinationName, message);
	}
	
}
