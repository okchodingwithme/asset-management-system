package kh.gov.treasury.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kh.gov.treasury.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpUserDetailsService implements UserDetailsService {
	 
    private final UserDao dao;    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Optional<HttpUserDetailsRequest> userRequestDetails = dao.authenticate(username);
    	if(!userRequestDetails.isPresent()) {
    		throw new UsernameNotFoundException("User not found");
    	}    	
    	HttpUserDetails userDetails = HttpUserDetails.builder()
						        	  .request(userRequestDetails.get())
						        	  .build();    	
    	log.info(userDetails.toString());    	
        return userDetails;
    }	
}