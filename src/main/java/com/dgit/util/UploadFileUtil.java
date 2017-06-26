package com.dgit.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.dgit.controller.UploadController;

public class UploadFileUtil {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	/*썸네일 이미지 만들기*/
	public static String makeThumbnail(String uploadPath, String filename) throws IOException{
		String thumbnailName = "";
		
//		원본이미지 가져오기
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath, filename));
		
//		썸네일 이미지 데이터 만들기 (resizing)
//		 * 사이즈가 100보다 작으면 확대되지 않고 그대로 적용됨 
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		
//		빈 파일 만들기
		thumbnailName = uploadPath + "/" + "s_" + filename;
		File newFile = new File(thumbnailName);
		
		String format = filename.substring(filename.lastIndexOf(".")+1); //.jpg, .png, .gif ...
		
//		thumbnail '경로/파일이름' 에 resizing된 이미지 적용
		ImageIO.write(destImg, format, newFile);
		
		return "s_" + filename;
	}//makeThumbnail
	
	/*upload와 동시에 썸네일 이미지 만들기*/
	public static String uploadFile(String uploadPath, //C:/zzz/upload 
									String originalName, //xxx.png 	
									byte[] fileData) //file data 
											throws IOException{
//		-------------------------------
//		upload 수행
//		-------------------------------
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalName;
		String savedPath = calPath(uploadPath); //최종 저장될 날 짜 받아옴
		
		File target = new File(uploadPath+savedPath, savedName);
		FileCopyUtils.copy(fileData, target);
		
//		-------------------------------
//		썸네일 이미지 만들기
//		-------------------------------
		String thumbFile = UploadFileUtil.makeThumbnail(uploadPath+savedPath, savedName);
		
		return savedPath + "/" + thumbFile; // 2017/04/24/xxx.jpg
	}//uploadFile

	/*날짜별 폴더 만들기 ex) C:/zzz/upload/2017/04/24 */
	private static String calPath(String uploadPath){
		Calendar cal = Calendar.getInstance();
		
		String yearPath = "/"+cal.get(Calendar.YEAR); // /2017
		String monthPath = String.format("%s/%02d", yearPath, cal.get(Calendar.MONTH) + 1); // 2017/04
		String datePath = String.format("%s/%02d", monthPath, cal.get(Calendar.DATE)); // 2017/04/24
		
		makeDir(uploadPath, yearPath, monthPath, datePath); //call method
		
//		최종 파일이 저장될 경로 : ex) 2017/04/24
		return datePath; 
	}//calPath

	private static void makeDir(String uploadPath, String... paths){
		for (String path : paths) {
			File dirPath = new File(uploadPath + path);
			
			if ( !dirPath.exists() ) {
				dirPath.mkdir();
			}
		}
	}//makeDir
}
