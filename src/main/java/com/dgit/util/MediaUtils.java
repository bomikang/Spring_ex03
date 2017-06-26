package com.dgit.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

/*안녕하세여 .xxx의 파일을 이미지로 변경해주는 클래스*/
public class MediaUtils {
	private static Map<String, MediaType> mediaMap;
	
	static{
		mediaMap = new HashMap<>();
		mediaMap.put("jpg", MediaType.IMAGE_JPEG);
		mediaMap.put("png", MediaType.IMAGE_PNG);
		mediaMap.put("gif", MediaType.IMAGE_GIF);
//		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
//		mediaMap.put("PNG", MediaType.IMAGE_PNG);
//		mediaMap.put("GIF", MediaType.IMAGE_GIF);
	}
	
	public static MediaType getMediaType(String type){
//		return mediaMap.get(type.toUpperCase());
		return mediaMap.get(type.toLowerCase());
	}
}
