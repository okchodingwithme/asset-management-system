package kh.gov.treasury.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;


@Builder
public record AuthLoginRequest (
    @NotEmpty(message = "Email should not be empty")
    @Email
    String email,  
    @NotEmpty(message = "password should not be empty")
    String password
){    

}