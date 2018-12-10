package com.petsshop.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("ConsumerSource")
	private String destinationName;
	
	//@JmsListener(destination = "ConsumerSource")
	public String receive() {
		return (String) jmsTemplate.receiveAndConvert(destinationName);
	}
}
