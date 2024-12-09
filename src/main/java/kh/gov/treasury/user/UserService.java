package kh.gov.treasury.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import kh.gov.treasury.utils.FileService;
import kh.gov.treasury.utils.ResultMessage;
import kh.gov.treasury.utils.ResultObject;
import kh.gov.treasury.utils.UserReqeustDetails;

@Service
public class UserService {
	
	@Autowired private UserDao dao;	
	@Autowired private PasswordEncoder passwordEncoder;	
	@Autowired FileService fileService;
	@Autowired UserReqeustDetails userReqeustDetails;
	
	public ResultObject<?> getUserSetting() {			
		return dao.userSetting(UserSettingDto.builder().build(), "READ");		
	}
	
	public ResultObject<?> updateUserSetting(UserSettingDto request) {	
		if (!ObjectUtils.isEmpty(request.getPassword())) {
			request.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		return dao.userSetting(request, "UPDATE");	
	}
	
	public ResultMessage updateUserPhoto(MultipartFile thumbnail) {		
       Map<String, Object> result = fileService.uploadSingleImage(thumbnail, "users");
       ResultMessage resultMessage = ResultMessage.builder().build();
       if(result.get("isUploaded").equals(true)) {
    	   String fileName = (String) result.get("fileName");
    	   resultMessage = dao.updateUserPhoto(fileName);
    	   userReqeustDetails.updateUserPhoto(fileName);
       }else if(result.get("isUploaded").equals(false)) {
    	   resultMessage = ResultMessage.builder()
    			   .isSuccessful(false)
    			   .message("Failed saving image!")
    			   .build();
       }       
	   return resultMessage;	
	}
	
}

