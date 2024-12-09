package kh.gov.treasury.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import kh.gov.treasury.config.HttpUserDetailsRequest;
import kh.gov.treasury.utils.FileService;
import kh.gov.treasury.utils.ResultMessage;
import kh.gov.treasury.utils.ResultObject;
import kh.gov.treasury.utils.Status;
import kh.gov.treasury.utils.UserReqeustDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserDao {
	
	 @Autowired private DataSource dataSource;	 
	 @Autowired private UserReqeustDetails uReqDtl;
	 @Autowired private FileService fileService;
	 
	 /* 
	  * Find User for session
	  * @param usernanme
	  * @return HttpUserDetailsRequest
	  * 
	  * */
	 public HttpUserDetailsRequest userSession(String username) {	
		 
		 String password = null;
		 String photo = null;
		 String name = null;
		 Set<SimpleGrantedAuthority> roles = new HashSet<SimpleGrantedAuthority>();
		 Set<SimpleGrantedAuthority> permissions = new HashSet<SimpleGrantedAuthority>();
		
		try (
			 Connection con = dataSource.getConnection();	 
	         CallableStatement cstmt = con.prepareCall("{call proc_user_session(?)}");
		){			
			 cstmt.setString("p_email", username);
			 ResultSet rs = cstmt.executeQuery();	 
			 while(rs.next()) {	
				  username = rs.getString("email");
				  password = rs.getString("password");
				  name = rs.getString("name");
				  photo = rs.getString("photo");
                  roles.add(new SimpleGrantedAuthority(rs.getString("role_name")));
                  permissions.add(new SimpleGrantedAuthority(rs.getString("permission_name")));
		    }					 
		    cstmt.close();	
		} catch (SQLException e) {			
			e.printStackTrace();
		} catch (Exception e) {				
			e.printStackTrace();	
		} 		
		
		HttpUserDetailsRequest response = HttpUserDetailsRequest.builder()
				.username(username)
				.password(password)
				.name(name)
				.photo(photo)
				.roles(roles)
				.permissions(permissions)
				.build();
		
		return response;
	}
	 
	 /* 
	  * Find User for Authentication
	  * @param usernanme
	  * @return HttpUserDetailsRequest
	  * 
	  * */
	 public Optional<HttpUserDetailsRequest> authenticate(String username) {	
		 
		 String password = null;
		 Set<SimpleGrantedAuthority> roles = new HashSet<SimpleGrantedAuthority>();
		 Set<SimpleGrantedAuthority> permissions = new HashSet<SimpleGrantedAuthority>();
		
		try (
			 Connection con = dataSource.getConnection();	 
	         CallableStatement cstmt = con.prepareCall("{call proc_user_auth(?)}");
		){			
			 cstmt.setString("p_email", username);
			 ResultSet rs = cstmt.executeQuery();	 
			 while(rs.next()) {	
				  username = rs.getString("email");
				  password = rs.getString("password");
                  roles.add(new SimpleGrantedAuthority(rs.getString("role_name")));
                  permissions.add(new SimpleGrantedAuthority(rs.getString("permission_name")));
		    }					 
		    cstmt.close();	
		} catch (SQLException e) {			
			e.printStackTrace();
		} catch (Exception e) {				
			e.printStackTrace();	
		} 		
		
		Optional<HttpUserDetailsRequest> response = Optional.of(HttpUserDetailsRequest.builder()
				.username(username)
				.password(password)
				.roles(roles)
				.permissions(permissions)
				.build());
		
		return response;
	}
	 
	 /* 
	  * For read/update user profile ( user setting )
	  * @param reDto
	  * @return UserSettingDto
	  * 
	  * */
	 public ResultObject<?> userSetting(UserSettingDto reDto, String command){
		 Boolean isSuccessful = false;
		 String message = "Successful.";
		 Integer code = null;
		 UserSettingDto data = new UserSettingDto();
		 String username = uReqDtl.getUsername();
		 
		 try (
				 Connection con = dataSource.getConnection();	 
		         CallableStatement cstmt = con.prepareCall("{call proc_user_setting(?,?,?,?,?,?,?,?,?)}");
		 ){			
			 
				 cstmt.setString("p_command", command);
				 cstmt.setString("p_username", username);
				 cstmt.setString("p_name", reDto.getName());
				 cstmt.setString("p_email", reDto.getEmail());
				 cstmt.setString("p_password", reDto.getPassword());
				 cstmt.setString("p_phone", reDto.getPhone());
				 cstmt.setString("p_photo", reDto.getPhoto());	
				 cstmt.registerOutParameter("status", Types.VARCHAR);	
				 cstmt.registerOutParameter("message", Types.VARCHAR);		
				 
			     ResultSet rs = cstmt.executeQuery();
			     
				 String status = cstmt.getString("status");			
				 message = cstmt.getString("message");	
				 
				 if(Status.SUCCESS.name().equals(status)) {	
					 isSuccessful = true;
					 code = HttpStatus.OK.value();
					 while(rs.next()) {					 
						 data.setName(rs.getString("name"));
						 data.setEmail(rs.getString("email"));
						 data.setPhone(rs.getString("phone"));
						 data.setPhoto(rs.getString("photo"));					 
				     }							 
			         log.info("(userSetting)({}) - {}", command, data);
				 }else {	
					 isSuccessful = false;
					 code = HttpStatus.BAD_REQUEST.value();
			         log.error("(userSetting) ({}) ({}) - {}", command, message, data);
				 }
			     cstmt.close();	
			     
		} catch (SQLException e) {			
				e.printStackTrace();
		} catch (Exception e) {				
				e.printStackTrace();	
		} 			 
		 
		return ResultObject.builder()
				 .isSuccessful(isSuccessful)
				 .code(code)
				 .timestamp(LocalDateTime.now())
				 .message(message)
				 .payload(data)
				 .build();
		 
	 }
	 
	 public ResultMessage updateUserPhoto(String fileName) {	
		 
		 Boolean isSuccessful = false;
		 String message = null;
		 Integer code = null;
		 String username = uReqDtl.getUsername();
		 
		 try (
				 Connection con = dataSource.getConnection();	 
		         CallableStatement cstmt = con.prepareCall("{call proc_user_setting_image (?,?,?,?,?)}");
		 ){						 
				 cstmt.setString("p_username", username);;
				 cstmt.setString("p_photo", fileName);	
				 cstmt.registerOutParameter("status", Types.VARCHAR);	
				 cstmt.registerOutParameter("message", Types.VARCHAR);	
				 cstmt.execute();			     
				 String status = cstmt.getString("status");		
				 String oldPhoto = cstmt.getString("old_photo");		
				 message = cstmt.getString("message");					 
			     cstmt.close();	
			     if(Status.SUCCESS.name().equals(status)) {
			    	 isSuccessful = true;
					 code = HttpStatus.OK.value();
					 //fileService.deleteSingleImage(oldPhoto);
			     }else {
			    	 code = HttpStatus.BAD_REQUEST.value();
			     }
		 } catch (SQLException e) {	
			    code = HttpStatus.INTERNAL_SERVER_ERROR.value();
			    message = e.getMessage();
				e.printStackTrace();
		 } catch (Exception e) {			
			    code = HttpStatus.INTERNAL_SERVER_ERROR.value();	
			    message = e.getMessage();
				e.printStackTrace();	
		 } 	
		 
		 return ResultMessage.builder()
				 .isSuccessful(isSuccessful)
				 .code(code)
				 .message(message)
				 .timestamp(LocalDateTime.now())
				 .build();
	 }
}
