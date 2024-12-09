package kh.gov.treasury.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
	
	@GetMapping("/login")
    public String loginPage(@RequestParam(name = "loginRequired", required = false) final Boolean loginRequire,
            @RequestParam(name = "loginError", required = false) final Boolean loginErro,
            @RequestParam(name = "logoutSuccess", required = false) final Boolean logoutSucces,
            final Model model) {
        model.addAttribute("authentication", AuthLoginRequest.builder().build());
        if (loginRequire == Boolean.TRUE) {
            model.addAttribute("isError", false);
        }else if (loginErro == Boolean.TRUE) {
            model.addAttribute("isError", true);
        }else if(logoutSucces == true) {
            model.addAttribute("isLogout", true);
        }
        
        return "auth/login";
    }
	
	// Redirect to home page
	@GetMapping({"","/","/error"})
    public String handleCssRequest() {
		return "redirect:/home";
    }
	
}
