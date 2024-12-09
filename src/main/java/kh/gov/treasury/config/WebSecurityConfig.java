package kh.gov.treasury.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {	
	
    private final AuthSuccessHandler authSuccessHandler;
    
    @Bean
    SecurityFilterChain configure(final HttpSecurity http) throws Exception {
         http
             .cors(withDefaults())
             .csrf(withDefaults())             
             .authorizeHttpRequests(
        		authorize -> authorize
        		.requestMatchers(
        				"/assets/**","/images/**",
        				"/","/login","/language"
        		).permitAll()
        		.anyRequest().authenticated()
        	 )         
             .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .successHandler(authSuccessHandler)
                .failureUrl("/login?loginError=true")
                .permitAll()
              )             
              .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logoutSuccess=true")
                .deleteCookies("JSESSIONID")
              )
              .exceptionHandling(
                 exception -> exception
                 .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true"))
              );       
         
         return http.build();
    }
     
}