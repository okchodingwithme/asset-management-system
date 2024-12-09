package kh.gov.treasury.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kh.gov.treasury.config.HttpUserDetailsRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserReqeustDetails {

	@Autowired
    private HttpServletRequest request;	
	
	@Autowired
	private HttpSession session;

    public String getClientIp() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
        	ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Anonymous";
    }  
    
    public HttpUserDetailsRequest getUserRequestDetails() {
    	return (HttpUserDetailsRequest) request.getSession().getAttribute("uReqDtl");
    }
    
    public void updateUserPhoto(String fileName) {    
    	HttpUserDetailsRequest uReqDtl =  (HttpUserDetailsRequest) request.getSession().getAttribute("uReqDtl");
    	uReqDtl.setPhoto(fileName);
    	session.setAttribute("uReqDtl", uReqDtl);
    }
}
