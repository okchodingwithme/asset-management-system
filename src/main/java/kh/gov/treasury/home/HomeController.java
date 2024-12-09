package kh.gov.treasury.home;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import kh.gov.treasury.utils.UserReqeustDetails;

@Controller
public class HomeController {

	@Autowired LocaleResolver localeResolver;
	@Autowired private UserReqeustDetails uReqDtl;
	
	@GetMapping("/home")
	public String home(Model model) {	
		model.addAttribute("uReqDtl", uReqDtl.getUserRequestDetails());
		model.addAttribute("lang", LocaleContextHolder.getLocaleContext());		
		return "home/home";
	}
	
	@GetMapping("/language")
	public String changeLanguage(@RequestParam("lang") String language) {
	    LocaleContextHolder.setLocale(Locale.forLanguageTag(language));    
	    return "redirect:/";
	}

}
