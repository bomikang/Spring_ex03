package com.dgit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dgit.util.MediaUtils;
import com.dgit.util.UploadFileUtil;

@Controller
public class UploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	private String innerUploadPath = "resources/upload";
	
	/*servlet-context.xml의 bean의 id값으로 주입 받아짐*/
	@Resource(name="uploadPath") 
	private String uploadPath;

	@RequestMapping(value = "/innerUpload", method = RequestMethod.GET)
	public String innerUploadForm() {
		return "innerUploadForm";
	}//innerUploadForm

	@RequestMapping(value = "/innerUpload", method = RequestMethod.POST)
	public String innerUploadFormResult(String writer, MultipartFile file, HttpServletRequest request, Model model) throws IOException {
		System.out.println("--------------------------------------------");
		logger.info("writer : "+writer);
		logger.info(file.getOriginalFilename());
		logger.info(file.getSize() + "");
		logger.info(file.getContentType());
		System.out.println("--------------------------------------------");

		/*서버 root절대경로*/
		String root_path = request.getSession().getServletContext().getRealPath("/");

		/*업로드폴더만들기*/
		File dir = new File(root_path + "/" + innerUploadPath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		/*파일이름중복처리 : 이미 업로드된 이미지를 위한 처리*/
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + file.getOriginalFilename(); // uid(randomkey)가 붙어서 파일이름 중복될일없음
		
		/*파일 업로드 진행*/
		File target = new File(root_path + "/" + innerUploadPath, savedName);
		FileCopyUtils.copy(file.getBytes(), target);

		model.addAttribute("writer", writer);
		model.addAttribute("filename", innerUploadPath + "/" + savedName);

		return "innerUploadResult";
	}//innerUploadResult

	@RequestMapping(value = "/innerMultiUpload", method = RequestMethod.GET)
	public String innerMultiUploadForm() {
		return "innerMultiUploadForm";
	}//innerMultiUploadForm

	@RequestMapping(value = "/innerMultiUpload", method = RequestMethod.POST)
	public String innerMultiUploadFormResult(String writer, List<MultipartFile> files, HttpServletRequest request, Model model) throws IOException {
		System.out.println("--------------------------------------------");
		logger.info("writer : "+writer);
		for (MultipartFile file : files) {
			logger.info(file.getOriginalFilename());
			logger.info(file.getSize() + "");
			logger.info(file.getContentType());
		}
		System.out.println("--------------------------------------------");
				
		String root_path = request.getSession().getServletContext().getRealPath("/");

		/*폴더가 존재하지 않을시 폴더 생성*/
		File dir = new File(root_path + "/" + innerUploadPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		ArrayList<String> fileNames = new ArrayList<>(); 
		for (MultipartFile file : files) {
			UUID uid = UUID.randomUUID();
			String savedName = uid.toString() + "_" + file.getOriginalFilename(); // uid(randomkey)가 붙어서 파일이름 중복될일없음
			
			File target = new File(root_path + "/" + innerUploadPath, savedName);
			FileCopyUtils.copy(file.getBytes(), target);
			
			fileNames.add(innerUploadPath + "/" + savedName);
		}
		
		model.addAttribute("writer", writer);
		model.addAttribute("files", fileNames);

		return "innerMultiUploadResult";
	}//innerMultiUploadResult
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.GET)
	public String uploadForm(){
		return "outerUploadForm";
	}
	
	/*서버 안에 파일을 넣는 것이 아니기 때문에 HttpRequest를 받아올 필요 X*/ 
	@RequestMapping(value="/uploadForm", method=RequestMethod.POST)
	public String uploadFormResult(MultipartFile file, String writer, Model model) throws IOException{
		logger.info("------------------------------------");
		logger.info(file.getOriginalFilename());
		logger.info(file.getSize() + "");
		logger.info(file.getContentType());
		logger.info(writer);
		
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + file.getOriginalFilename(); //uid(randomkey)가 붙어서 파일이름 중복될일없음
		
		/*file upload : C:/zzz/upload/ */
		File target = new File(uploadPath, savedName);
		FileCopyUtils.copy(file.getBytes(), target);
		
		/*썸네일로 보이기*/
		String thumbFile = UploadFileUtil.makeThumbnail(uploadPath, savedName); //method호출
		
		model.addAttribute("writer", writer);
		model.addAttribute("filename", thumbFile);
		
		return "outerUploadResult";
	}//uploadFormResult
	
	@RequestMapping(value="/multiUploadForm", method=RequestMethod.GET)
	public String uploadMultiForm(){
		logger.info("multi upload form get---------------------------");
		return "outerMultiUploadForm";
	}//uploadMultiForm
	
	@RequestMapping(value="/multiUploadForm", method=RequestMethod.POST)
	public String uploadMultiFormResult(List<MultipartFile> files, String writer, Model model) throws IOException{
		logger.info("------------------------------------");
		for (MultipartFile file : files) {
			logger.info(file.getOriginalFilename());
			logger.info(file.getSize() + "");
			logger.info(file.getContentType());
			logger.info(writer);
		}
		
		ArrayList<String> fileNames = new ArrayList<>();
		
		for (MultipartFile file : files) {
			UUID uid = UUID.randomUUID();
			String savedName = uid.toString() + "_" + file.getOriginalFilename();
			
			File target = new File(uploadPath, savedName);
			FileCopyUtils.copy(file.getBytes(), target);
			
			String thumbFile = UploadFileUtil.makeThumbnail(uploadPath, savedName);
			fileNames.add(thumbFile);
		}
		
		model.addAttribute("writer", writer);
		model.addAttribute("fileNames", fileNames);
		
		return "outerMultiUploadResult";
	}//uploadMultiFormResult
	
	/*외부 이미지 파일 보이도록 하는 메소드*/
	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName){
		InputStream inS = null;
		ResponseEntity<byte[]> entity = null;
		
		logger.info("[displayFile] fileName : "+fileName);
		
//		파일확장자만 뽑기
		String format = fileName.substring(fileName.lastIndexOf(".")+1);
		
//		패키지 com.dgit.util의 메소드를 불러와 
//		HttpHeaders에 주입
		MediaType mType = MediaUtils.getMediaType(format);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mType); //png, jpg, gif ...
		
		try {
			inS = new FileInputStream(uploadPath + "/" + fileName);
			
//			IOUtils.toByteArray(inS); : 바이트배열로 만들어주는 메소드
			entity = new ResponseEntity<>(IOUtils.toByteArray(inS), headers, HttpStatus.CREATED);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			try {
				inS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}
}
