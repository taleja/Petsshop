package com.petsshop.hibernate;

import org.hibernate.Session;

import com.petsshop.model.Product;



public class PersistToDB {

	public static void main(String[] args) {
		
		//PersistToDB.class.getClassLoader().getResource(name)
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Product product = new Product();
		product.setProductName("mouse11");
		product.setDescription("toy11");
		product.setQuantity(5);
		
		session.save(product);
		session.getTransaction().commit();		
	}

}
