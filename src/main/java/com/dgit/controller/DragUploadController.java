package com.dgit.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dgit.util.UploadFileUtil;

@Controller
public class DragUploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Resource(name="uploadPath")
	private String uploadPath;
	
	@RequestMapping(value = "/dragUpload", method = RequestMethod.GET)
	public String dragUploadForm() {
		return "dragUploadForm";
	}//dragUploadForm
	
	@ResponseBody //return으로 data(entity)만 보낼 때
	@RequestMapping(value = "/dragUpload", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<String>> dragUploadFormResult(List<MultipartFile> files, String writer) throws IOException {
		/*files : dragUploadForm에서  formdata가 넘긴 files, writer : dragUploadForm에서  formdata가 넘긴 writer*/	
		
		ResponseEntity<ArrayList<String>> entity = null;
		
		try{
			System.out.println("-----------------------------------------------------------");
			logger.info("writer : "+writer);
			for (MultipartFile file : files) {
				logger.info(file.getOriginalFilename());
				logger.info(file.getSize() + "");
				logger.info(file.getContentType());
			}
			System.out.println("-----------------------------------------------------------");
			
			/*upload실행*/
			ArrayList<String> fileNames = new ArrayList<>();
			for (MultipartFile file : files) {
//				업로드와 동시에 썸네일 만들기 메소드 호출
				String thumbFile = UploadFileUtil.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
				
				logger.info("================="+thumbFile);
				fileNames.add(thumbFile);
			}
			
			entity = new ResponseEntity<>(fileNames, HttpStatus.OK); //200
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
		}
		
		return entity;
	}//dragUploadForm

	
	/*이미지 파일 지우기*/
	@ResponseBody
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName) throws IOException {
		ResponseEntity<String> entity = null;
		
		System.out.println("--------------------------------");
//		현재파일(썸네일파일) : /2017/04/24/s_xxx.png
		logger.info("delete file name : " + fileName);
		
		try{
			/*원본파일지우기('s_'를 뺀 파일명이 원본 파일명) : /2017/04/24/xxx.png */
			String calDir = fileName.substring(0, 12); // /2017/04/24/
			String realName = fileName.substring(14); // xxx.png
			new File(uploadPath + calDir + realName).delete();
			
			/*썸네일파일지우기*/
			new File(uploadPath + fileName).delete();
			
			entity = new ResponseEntity<>("delete SUCCESS", HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}//deleteFile
}
