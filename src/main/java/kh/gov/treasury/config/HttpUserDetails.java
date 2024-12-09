package kh.gov.treasury.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;

@Builder
public record HttpUserDetails(HttpUserDetailsRequest request) implements UserDetails {
	
    @Override
    public String getUsername() {
        return request.getUsername();
    }
    
	@Override
    public String getPassword() {
        return request.getPassword();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
    	Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(request.getRoles());
        authorities.addAll(request.getPermissions());
        return authorities;
    }
    
}