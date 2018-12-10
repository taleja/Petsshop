package com.petsshop.dto;

public class FileUploadStatusDTO {

	private Integer totalNumberOfFroducts;
	private Integer failNumberOfProducts;
	
	public Integer getTotalNumberOfFroducts() {
		return totalNumberOfFroducts;
	}
	public void setTotalNumberOfFroducts(Integer totalNumberOfFroducts) {
		this.totalNumberOfFroducts = totalNumberOfFroducts;
	}
	public Integer getFailNumberOfProducts() {
		return failNumberOfProducts;
	}
	public void setFailNumberOfProducts(Integer failNumberOfProducts) {
		this.failNumberOfProducts = failNumberOfProducts;
	}
	
	
}
