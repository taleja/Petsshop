package com.petsshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.petsshop.jms.Consumer;
import com.petsshop.jms.Producer;
import com.petsshop.model.Product;

@Service
public class JmsClientService {
	
	public static final String DEFAULT_DESTINATION = "TestDestination";
	@Autowired
	private Producer producer;
	@Autowired
	private Consumer consumer;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	public String receive() {
		System.out.println("Sent from consumer");
		return consumer.receive();
	}
	
	public void send(String message) {
		System.out.println("Received from producer");
		producer.sendMessage(message);
	}
	
	public void sendProductToBrocker(Product product) {
		jmsTemplate.convertAndSend(DEFAULT_DESTINATION, product);
	}

}
