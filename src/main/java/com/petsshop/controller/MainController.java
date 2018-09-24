package com.petsshop.controller;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.petsshop.model.Product;
import com.petsshop.service.ProductService;

@Controller
@RequestMapping("/welcome")
public class MainController {
	
	//protected static Logger logger = Logger.getLogger(MainController.class);
	
	@Resource(name="productService")
	private ProductService productService;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	//@GetMapping("/products")
	public String getProducts(Model model) {
		//logger.debug("Received request to show all products");
		List<Product> products = productService.getAll();
		model.addAttribute("products", products);
		//Return name of jsp page WEB-INF/jsp/productspage.jsp	
		return "productspage";
	}
	
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute("productAttribute") Product product) {
		productService.add(product);
		return "redirect:/petsshop/welcome/products";
	}
	
	@RequestMapping(value = "/addProduct", method = RequestMethod.GET)
	public String addProduct(Model model) {
		model.addAttribute("productAttribute", new Product());
		return "addPage";
	}
	
	@RequestMapping(value = "/editProduct", method = RequestMethod.POST)
	public String saveEdit(@ModelAttribute("productAttribute") Product product, @RequestParam(value = "id", required = true)Integer id, 
			Model model) {
		product.setId(id);
		productService.edit(product);
		model.addAttribute("id", id);
		return "editedPage";
	}
	
	@RequestMapping(value = "/editProduct", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true)Integer id, Model model) {
		model.addAttribute("productAttribute", productService.get(id));
		return "editPage";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public String delete(@RequestParam(value = "id", required =  true) Integer id, Model model) {
		productService.delete(id);
		model.addAttribute("id", id);
		return "deletedPage";
		
	}

	@PostConstruct
	public void print(){ 
		System.out.println("----Print post construct 6---");
	}
}	
