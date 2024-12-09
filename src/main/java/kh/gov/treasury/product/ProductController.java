package kh.gov.treasury.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kh.gov.treasury.utils.ResultObject;
import kh.gov.treasury.utils.UserReqeustDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {
	
	private static final String PRODUCT_LIST = "product/product_list";
	private static final String PRODUCT_CREATE = "product/product_create";

	@Autowired private UserReqeustDetails uReqDtl;
	@Autowired private ProductService productService;
	
	@GetMapping("/list")
	public String list(Model model) {		
		model.addAttribute("uReqDtl", uReqDtl.getUserRequestDetails());		
		ProductResponse<?> products = productService.productList(ProductRequest.builder().page(1).size(10).build());	
		model.addAttribute("products", products);		
		return PRODUCT_LIST;
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("uReqDtl", uReqDtl.getUserRequestDetails());
		return PRODUCT_CREATE;
	}
	
	// Update user setting
	@PostMapping("/serial")
	@ResponseBody
	public ResponseEntity<?> productFilterSerial(
			@RequestBody ProductSerilsRequest request
		){		
		ResultObject<?> response = productService.filterProductSerils(request);
		log.info("(udpateUserSetting) - {}",response);		
		return ResponseEntity.ok(response);
		
	}	
	
	@GetMapping("/distribute")
	public String distribute(Model model) {
		model.addAttribute("uReqDtl", uReqDtl.getUserRequestDetails());
		return "product/product_distribute";
	}
	
}
