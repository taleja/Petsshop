package com.petsshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.petsshop.dto.ProductDTO;
import com.petsshop.dto.ProductDetailDTO;
import com.petsshop.model.Product;
import com.petsshop.service.ProductService;

@RequestMapping("/rest")
@Controller
public class RestController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductDTO> all(@RequestParam(name = "offset", required = false, defaultValue = "0") int offset, 
			@RequestParam(name = "limit", required = false, defaultValue = "1000") int limit) {
		return productService.getAllDTO(offset, limit);
	}
	
	@RequestMapping(value = "products/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ProductDTO get(@PathVariable(name = "id") Integer id) {		
		return productService.getDTO(id); 
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	@ResponseBody
	public ProductDTO post(@RequestBody ProductDetailDTO productDetail) {
		Integer id = productService.addDTO(productDetail);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setDetail(productDetail);
		productDTO.setId(id);
		return productDTO;
	}
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(name = "id") Integer id) {
		productService.delete(id);
	} 
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ProductDTO edit(@PathVariable(name = "id") Integer id, @RequestBody ProductDetailDTO productDetail) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(id);
		productDTO.setDetail(productDetail);		
		productService.updateDTO(productDTO);  
		return productDTO;
	}
}
