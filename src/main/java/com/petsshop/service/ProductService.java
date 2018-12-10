package com.petsshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petsshop.dto.ProductDTO;
import com.petsshop.dto.ProductDetailDTO;
import com.petsshop.model.Product;

@Service(value="productService")
@Transactional
public class ProductService {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFacroty;
	
	@Autowired 
	private JmsClientService jmsClientService;
	
	public List<Product> getAll(){

		Session session = getSession();
		Query<Product> query = session.createQuery("From Product");	
		return query.list();
	}

	private Session getSession() {
		Session session = sessionFacroty.getCurrentSession();
		return session;
	}
	
	public Product get(Integer id) {
		Session session = getSession();
		Product product = session.get(Product.class, id);
		return product;
	}
	
	public void add(Product product) {
		Session session = getSession();
		session.save(product);
	}
	
	public void delete(Integer id) {	
		Session session = getSession();
		Product product = session.get(Product.class, id);
		session.delete(product);
	}
	
	public void edit(Product product) {
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
		Product product;
		
		Query<Product> query = session.createQuery("from Product as product where product.productName=:product_name");
		query.setParameter("product_name", productDetail.getProductName());
		if(query.list().size() == 0) {
			product = new Product();
			product.setProductName(productDetail.getProductName());
			product.setDescription(productDetail.getDescription());
			product.setQuantity(productDetail.getQuantity());
			session.save(product);
		} else {
			product = query.getSingleResult();
			product.setProductName(productDetail.getProductName());
			product.setDescription(productDetail.getDescription());
			int newQuantity = product.getQuantity() + productDetail.getQuantity();
			product.setQuantity(newQuantity);
			session.save(product);
		}	
		jmsClientService.sendProductToBrocker(product);	
		return product.getId();
	}

	public void addDTOList(List<ProductDetailDTO> productDetailDTOList) {
		Session session = getSession();
		session.setJdbcBatchSize(productDetailDTOList.size());
		List<String> productNames = new ArrayList<>();
		Map<String, ProductDetailDTO> productNameToProductDTO = new HashMap<>();
		
		for(ProductDetailDTO pdd: productDetailDTOList) {
			productNames.add(pdd.getProductName());
			productNameToProductDTO.put(pdd.getProductName(), pdd);
		}
		
		Query<Product> query = session.createQuery("from Product as product where product.productName in (:product_names)");	
		query.setParameterList("product_names", productNames);
		List<Product> existedProducts = query.list();
		
		//Update existing products
		for(Product existingProduct: existedProducts) {
			ProductDetailDTO prodDTO = productNameToProductDTO.remove(existingProduct.getProductName());
			existingProduct.setDescription(prodDTO.getDescription());
			existingProduct.setQuantity(existingProduct.getQuantity() + prodDTO.getQuantity()); 
		}
		session.flush();
		
		//Create new products
		for(ProductDetailDTO newProductDTO: productNameToProductDTO.values()) {
			Product product = new Product();
			product.setProductName(newProductDTO.getProductName());
			product.setDescription(newProductDTO.getDescription());
			product.setQuantity(newProductDTO.getQuantity());
	
			session.save(product);
		}
		session.flush();
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

	public List<ProductDTO> getAllDTO(int offset, int limit) {
		Session session = getSession();
		Query<Product> query = session.createQuery("From Product");	
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		
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

