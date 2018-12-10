package com.petsshop.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.petsshop.dto.FileUploadStatusDTO;
import com.petsshop.service.FileUploadService;

@RequestMapping("/upload")
@Controller
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = "/file", method = RequestMethod.POST)
	@ResponseBody
	public FileUploadStatusDTO upload(@RequestParam(name = "file")MultipartFile file) throws JsonProcessingException, IOException, InterruptedException, ExecutionException {
		//return fileUploadService.importProductsFromFile(file.getInputStream()); 
		//return fileUploadService.importProductsFromFileInParallel(file.getInputStream());
		return fileUploadService.importProductsFromFileInParallelWithButch(file.getInputStream());
	}

}
