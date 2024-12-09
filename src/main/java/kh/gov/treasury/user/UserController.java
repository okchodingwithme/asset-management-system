package kh.gov.treasury.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kh.gov.treasury.utils.ResultObject;
import kh.gov.treasury.utils.UserReqeustDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired private UserService service;
	@Autowired private UserReqeustDetails uReqDtl;
	
	// Get user to display on User Setting Page
	@GetMapping("/setting")
	public String userSetting(Model model) {
		ResultObject<?> responseBase = service.getUserSetting();
		model.addAttribute("uReqDtl", uReqDtl.getUserRequestDetails());
		model.addAttribute("data",responseBase);
		return "user/user_setting";
	}
	
	// Update user setting
	@PostMapping("/setting/update")
	@ResponseBody
	public ResponseEntity<?> udpateUserSetting(
			@RequestBody UserSettingDto request
		){		
		ResultObject<?> responseBase = service.updateUserSetting(request);	
		log.info("(udpateUserSetting) - {}",request);		
		return ResponseEntity.ok(responseBase);
		
	}	
	
	// Update user setting
	@PostMapping("/setting/image")
	@ResponseBody
	public ResponseEntity<?> udpateUserPhoto(
			@RequestParam("file") MultipartFile thumbnail
		){		
		log.info("(udpateUserPhoto) - {}",thumbnail);			
		return ResponseEntity.ok(service.updateUserPhoto(thumbnail));
		
	}

}
