package kh.gov.treasury.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingDto {
	String name;
	String email;
	String password;
	String gender;
	String phone;
	String photo;
}