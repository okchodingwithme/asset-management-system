package kh.gov.treasury.config;

import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpUserDetailsRequest{
	String username;
	String password;
	String name;
	String photo;
	Set<SimpleGrantedAuthority> roles;
	Set<SimpleGrantedAuthority> permissions;
}
