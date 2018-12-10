package com.petsshop.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.petsshop.model.Product;

@Component
public class PetsshopMessageConverter implements MessageConverter {

	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		Product product = (Product) object;
		MapMessage message = session.createMapMessage();
		message.setInt("id", product.getId());
		message.setString("productName", product.getProductName());
		message.setString("description", product.getDescription());
		message.setInt("quantity", product.getQuantity());
		return message;
	}

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		MapMessage mapMessage  = (MapMessage) message;
		Product product = new Product();
		product.setProductName((mapMessage.getString("productName")));
		product.setDescription((mapMessage.getString("description")));
		product.setQuantity((mapMessage.getInt("quantity")));
		product.setId(mapMessage.getInt("id"));
		return product; 
	}
}
