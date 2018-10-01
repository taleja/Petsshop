package com.petsshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

//import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petsshop.dto.ProductDTO;
import com.petsshop.dto.ProductDetailDTO;
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

	public void updateDTO(ProductDTO productDTO) {
		
		Session session = getSession();
		Product existingProduct = session.get(Product.class, productDTO.getId());
		existingProduct.setProductName(productDTO.getDetail().getProductName());
		existingProduct.setDescription(productDTO.getDetail().getDescription());
		existingProduct.setQuantity(productDTO.getDetail().getQuantity());	
		session.save(existingProduct);		
	}

	public Integer addDTO(ProductDetailDTO productDetail) {
		Session session = getSession();
		Product product = new Product();
		product.setProductName(productDetail.getProductName());
		product.setDescription(productDetail.getDescription());
		product.setQuantity(productDetail.getQuantity());
		session.save(product);
		return product.getId();
	}

	public ProductDTO getDTO(Integer id) {
		Session session = getSession();
		Product product = session.get(Product.class, id);
		
		ProductDetailDTO productDetail = new ProductDetailDTO();
		productDetail.setProductName(product.getProductName());
		productDetail.setDescription(product.getDescription());
		productDetail.setQuantity(product.getQuantity());
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setDetail(productDetail);
		productDTO.setId(product.getId());
		return productDTO;
	}

	public List<ProductDTO> getAllDTO() {
		Session session = getSession();
		Query<Product> query = session.createQuery("From Product");	
		
		List<ProductDTO> listDTO = new ArrayList<>();
		for(Product product: query.list()) {
			
			ProductDetailDTO productDetail = new ProductDetailDTO();
			productDetail.setProductName(product.getProductName());
			productDetail.setProductName(product.getDescription());
			productDetail.setQuantity(product.getQuantity());
			
			ProductDTO productDTO = new ProductDTO();
			productDTO.setDetail(productDetail);
			productDTO.setId(product.getId());
			
			listDTO.add(productDTO);
		}		
		return listDTO;
	}
}

