package kh.gov.treasury.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kh.gov.treasury.user.UserDao;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired private UserDao dao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {  
        HttpSession session = request.getSession();
    	HttpUserDetailsRequest userRequest = dao.userSession(authentication.getName());
        session.setAttribute("uReqDtl", userRequest);     
        response.sendRedirect(request.getContextPath()+"/home");
    }
}