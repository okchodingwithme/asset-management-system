package kh.gov.treasury.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import kh.gov.treasury.utils.ResultObject;

@Service
public class ProductService {

	@Autowired ProductDao productDao;
	
	public ProductResponse<?> productList(ProductRequest request){		
		return productDao.productList(request);	
	}
	
	public ResultObject<?> filterProductSerils(
			 ProductSerilsRequest request
			){
		
		List<ProductSerials> prdSerials = productDao.getProductSerialsByProductId(request);
		
		return ResultObject.builder()
				.isSuccessful(true)
				.code(HttpStatus.OK.value())
				.message("Successfule")
				.timestamp(LocalDateTime.now())
				.payload(prdSerials)
				.build();
		
	}
}
