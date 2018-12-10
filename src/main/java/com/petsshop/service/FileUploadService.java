package com.petsshop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petsshop.dto.FileUploadStatusDTO;
import com.petsshop.dto.ProductDetailDTO;

@Service(value = "fileUploadService")
@Transactional
public class FileUploadService {
	
	private static final Logger LOG = Logger.getLogger(FileUploadService.class);
	private static final int BUTCH_SIZE = 10;
	@Autowired
	private ProductService productService;
	private ExecutorService executorService = Executors.newFixedThreadPool(1); 
	
	
	public FileUploadStatusDTO importProductsFromFile(InputStream productFileInputStream) throws IOException {
		List<String> result =  new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(productFileInputStream));){		
			String line;
			
			while((line = bufferedReader.readLine()) != null) {
				result.add(line);
				
			}
			return createProductDTO(result); 
		}
	}
	
	public FileUploadStatusDTO importProductsFromFileInParallel(InputStream productFileInputStream) throws IOException, InterruptedException, ExecutionException {
		List<String> result =  new ArrayList<>();
		List<Future<Boolean>> futuresList =  new ArrayList<>();
		
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(productFileInputStream));){		
			String line;			
			while((line = bufferedReader.readLine()) != null) {
				String currentLine = line;
				
				Future<Boolean> future = executorService.submit(()->{
					System.out.println(Thread.currentThread().getName());
					try {
						ProductDetailDTO pdd = convertToProductDTO(currentLine);
						productService.addDTO(pdd);				
					}catch(NumberFormatException ex){
						LOG.error("Error on proccesing line " + currentLine, ex);
						//ex.printStackTrace();	
						return false;
					}
					return true;
				});
				futuresList.add(future); 
			}			
		}
		int errorsCounter = 0;
		for(Future<Boolean> productProcessingResult: futuresList) {
			Boolean productResult = productProcessingResult.get();
			if(!productResult) {
				errorsCounter++;
			}
		}
		FileUploadStatusDTO fileUploadStatusDTO  = new FileUploadStatusDTO();
		fileUploadStatusDTO.setFailNumberOfProducts(errorsCounter);
		fileUploadStatusDTO.setTotalNumberOfFroducts(futuresList.size());
		return fileUploadStatusDTO;
	}
	
	public FileUploadStatusDTO importProductsFromFileInParallelWithButch(InputStream productFileInputStream) throws IOException, InterruptedException, ExecutionException {
		List<String> result =  new ArrayList<>();
		List<Future<FileUploadStatusDTO>> futuresList =  new ArrayList<>();
		
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(productFileInputStream));){		
			String line;	
			List<String> butch = new ArrayList<>(BUTCH_SIZE);
			while((line = bufferedReader.readLine()) != null) {
				String currentLine = line;
				butch.add(line);
				
				if(butch.size() >= BUTCH_SIZE) {
					List<String> currentButch = butch;
					butch = new ArrayList<>(BUTCH_SIZE);
					
					Future<FileUploadStatusDTO> future = executorService.submit(()->{
						LOG.debug(Thread.currentThread().getName()); 
						List<ProductDetailDTO> productDetailDTOList = convertToProductDTOs(currentButch);
						productService.addDTOList(productDetailDTOList);	 
						
						FileUploadStatusDTO fileUploadStatusDTO = new FileUploadStatusDTO(); 
						fileUploadStatusDTO.setFailNumberOfProducts(currentButch.size() - productDetailDTOList.size());
						fileUploadStatusDTO.setTotalNumberOfFroducts(currentButch.size());
						return fileUploadStatusDTO;
					});
					futuresList.add(future); 
				}				
			}			
		}
		int errorsCounter = 0;
		int totalCounter = 0;
		for(Future<FileUploadStatusDTO> productProcessingResult: futuresList) {
			errorsCounter = errorsCounter + productProcessingResult.get().getFailNumberOfProducts();
			totalCounter = totalCounter + productProcessingResult.get().getTotalNumberOfFroducts();
		}
		FileUploadStatusDTO finalFileUploadStatus  = new FileUploadStatusDTO();
		finalFileUploadStatus.setFailNumberOfProducts(errorsCounter);
		finalFileUploadStatus.setTotalNumberOfFroducts(totalCounter);
		return finalFileUploadStatus;
	}

	
	private FileUploadStatusDTO createProductDTO(List<String> dataFromFile) {
		ProductDetailDTO pdd;
		StringBuilder sb = new StringBuilder();
		String[] stringArray; 
		int failCounter = 0;
		for(String line: dataFromFile) {
			try {
				pdd = convertToProductDTO(line);
				productService.addDTO(pdd);				
			}catch(NumberFormatException ex){
				ex.printStackTrace();	
				failCounter++;
			}
		}	
		FileUploadStatusDTO fusDTO = new FileUploadStatusDTO();
		fusDTO.setFailNumberOfProducts(failCounter);
		fusDTO.setTotalNumberOfFroducts(dataFromFile.size());
		return fusDTO; 
	}
	
	private ProductDetailDTO convertToProductDTO(String line) {
		ProductDetailDTO pdd;
		String[] stringArray;
		stringArray = line.split(",");
		pdd = new ProductDetailDTO();
		pdd.setProductName(stringArray[0]);
		pdd.setDescription(stringArray[1]);
		pdd.setQuantity(Integer.parseInt(stringArray[2]));
		return pdd;
	}
	
	private List<ProductDetailDTO> convertToProductDTOs(List<String> lines) {
		List<ProductDetailDTO> productDetailDTOList = new ArrayList<>();
		for(String line: lines) {
			try {
				productDetailDTOList.add(convertToProductDTO(line));				
			}catch(NumberFormatException ex) {
				LOG.error("Error on proccesing line " + line, ex);
			}			
		}
		return productDetailDTOList;
	}

}
