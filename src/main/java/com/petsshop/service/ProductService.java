package com.petsshop.service;

import java.util.List;

import javax.annotation.Resource;

//import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petsshop.model.Product;



@Service(value="productService")
@Transactional
public class ProductService {

	//protected static Logger logger = Logger.getLogger("service");
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFacroty;
	
	public List<Product> getAll(){
		//logger.debug("Retrieving all products");
		
		Session session = getSession();
		Query<Product> query = session.createQuery("From Product");	
		return query.list();
	}

	private Session getSession() {
		Session session = sessionFacroty.getCurrentSession();
		return session;
	}
	
	public Product get(Integer id) {
		//logger.debug("Getting product by id");
		
		Session session = getSession();
		Product product = session.get(Product.class, id);
		return product;
	}
	
	public void add(Product product) {
		//logger.debug("Adding new product");
		
		Session session = getSession();
		session.save(product);
	}
	
	public void delete(Integer id) {
		//logger.debug("Delete product by id");
		
		Session session = getSession();
		Product product = session.get(Product.class, id);
		session.delete(product);
	}
	
	public void edit(Product product) {
		//logger.debug("Edit product");
		
		Session session = getSession();
		Product existingProduct = session.get(Product.class, product.getId());
		existingProduct.setProductName(product.getProductName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setQuantity(product.getQuantity());		
		session.save(existingProduct);
	}
}

