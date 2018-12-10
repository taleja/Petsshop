package com.petsshop.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.petsshop.dto.ProductDTO;
import com.petsshop.service.ProductService;

@Component
public class SimpleListener implements MessageListener{
	
	private static final Logger LOG = Logger.getLogger(SimpleListener.class);
	
	@Autowired
	private ProductService productService;

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
			try {
				String msg = ((TextMessage) message).getText();
				System.out.println("Message: " + msg);
			} catch(JMSException ex) {
				 LOG.info("Message error: " + ex);
			}
			
		}
		
	}
	
	@JmsListener(destination = "ConsumerSource")
	public void receiveMessage(ProductDTO message) {
		System.out.println("Message: " + message);
		productService.updateDTO(message);
	}

}
