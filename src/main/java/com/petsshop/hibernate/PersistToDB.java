package com.petsshop.hibernate;

import org.hibernate.Session;

import com.petsshop.model.Product;
import com.petsshop.model.User;



public class PersistToDB {

	public static void main(String[] args) {
		
		//PersistToDB.class.getClassLoader().getResource(name)
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
//		Product product = new Product();
//		product.setProductName("mouse11");
//		product.setDescription("toy11");
//		product.setQuantity(5);
		
//		User user = new User();
//		user.setLogin("oviliuzh");
//		user.setPassword("admin"); 
//		user.setRole("administartor");
//		session.save(user);
		
		User user = new User();
		user.setLogin("olena");
		user.setPassword("qwerty"); 
		user.setRole("guest");
		session.save(user);
		
		//session.save(product);
		session.getTransaction().commit();		
	}

}
