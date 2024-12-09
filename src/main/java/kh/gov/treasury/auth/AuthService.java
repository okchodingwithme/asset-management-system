package kh.gov.treasury.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kh.gov.treasury.config.HttpUserDetailsRequest;
import kh.gov.treasury.user.UserDao;



@Service
public class AuthService {

	@Autowired private UserDao dao;    
    @Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;   
	
    public static final int MAX_FAILED_ATTEMPTS = 5;
    
    	
	public void login(AuthLoginRequest request) {
	    validateCredentials(request.email(), request.password());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));  
	}	
	
    public Boolean validateCredentials(String email, String password) {		
		Optional<HttpUserDetailsRequest> uReqDtl = dao.authenticate(email);		
		if(!uReqDtl.isPresent()) {
			throw new BadCredentialsException("Invalid credentials");
		}		
		if (!passwordEncoder.matches(password, uReqDtl.get().getPassword())) { // If the password is incorrect			
            // // Increment failed attempts
			// int failedAttempts = uReqDtl.failedAttemps() + 1;
            // dao.updateFailedAttempts(username, failedAttempts, "NO");
            //if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // If the user has failed to log in 5 times
            //    // Lock the account
            //    log.info("Locking account for user: {}", username);
            //    dao.updateFailedAttempts(username, failedAttempts, "YES");
            //    throw new AccountLockedException();
            //}
            throw new BadCredentialsException("Invalid credentials");
        }
		// int failedAttempts = 0;
        // dao.updateFailedAttempts(username, failedAttempts, "NO");		
		return true;
		
	}
}
