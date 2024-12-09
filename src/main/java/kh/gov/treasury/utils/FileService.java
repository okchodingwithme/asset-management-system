package kh.gov.treasury.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileService {
	
	public Map<String, Object> uploadSingleImage(MultipartFile file, String dir) {		

        Map<String, Object> result = new HashMap<>();
        
	    String fileName = dir+"-"+UUID.randomUUID().toString();
        int lastIndexOfDot = file.getOriginalFilename().lastIndexOf(".");
        String extension = file.getOriginalFilename().substring(lastIndexOfDot);
        fileName += extension;
        
        System.out.println(extension);
        System.out.println(fileName);

        // Create path object
        Path path = Paths.get(GlobalProperties.FILE_SERVER_PATH + "/images/"+dir+"/"+ fileName);

        // Copy file to server path
        try {
            Files.copy(file.getInputStream(), path);
            result.put("isUploaded", true);
            result.put("fileName", "images/users/"+fileName);
            return result;
        } catch (IOException e) {
        	e.getMessage();
	    	result.put("isUploaded", false);
	        result.put("fileName", null);
	        return result;
        }
        
	}
	
	public void deleteSingleImage(String fileName) {
		 Path path = Paths.get(GlobalProperties.FILE_SERVER_PATH + fileName);
		 if (Files.exists(path)) {
			 try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	}
}
