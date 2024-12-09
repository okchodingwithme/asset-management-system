package kh.gov.treasury.product;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductDao {

	 @Autowired private DataSource dataSource;	 
	 

	 private List<String> pNames = new ArrayList<>();
	 private List<String> pSources = new ArrayList<>();
	 private List<String> pCats = new ArrayList<>();
	 
	 public ProductResponse<?> productList(ProductRequest request){
		 
		List<Product> pList = new ArrayList<Product>();	
	    int number = request.getPage();
	    int size = request.getSize();
	    long totalElements = 0;
	    int totalPages = 0;
	    int first = 0;
	    int last = 0;
	    
		 try (
			 Connection con = dataSource.getConnection();	 
	         CallableStatement cstmt = con.prepareCall("{call proc_product_list(?,?,?,?,?,?,?,?,?,?)}"); //10
		 ){			
			 
			 cstmt.setString("p_name", request.getName());
			 cstmt.setString("p_category", request.getCategory());
			 cstmt.setString("p_source", request.getSource());
			 cstmt.setString("p_quantity", request.getQty());		 
			 cstmt.setInt("p_page_number", number);
			 cstmt.setInt("p_page_size", size);
			 cstmt.registerOutParameter("total_records", Types.INTEGER);
			 cstmt.registerOutParameter("total_pages", Types.INTEGER);
			 cstmt.registerOutParameter("first_page", Types.INTEGER);
			 cstmt.registerOutParameter("last_page", Types.INTEGER);	
			 
			 ResultSet rs = cstmt.executeQuery();
     
             while (rs.next()) {
                Product pDto = Product.builder()
                    .productId(rs.getString("product_id"))
                    .productName(rs.getString("product_name"))
                    .categoryName(rs.getString("category_name"))
                    .deliveryDate(rs.getString("delivery_date"))
                    .quantity(rs.getString("quantity"))
                    .remain(rs.getString("remain"))
                    .cost(rs.getString("cost"))
                    .sourceName(rs.getString("source_name"))
                    .currency(rs.getString("currency"))
                    .image(rs.getString("image"))
                    .build();
                
                pList.add(pDto);
             }
             
		     // Retrieve output parameters
		     totalElements = cstmt.getInt("total_records");
		     totalPages = cstmt.getInt("total_pages");
		     first = cstmt.getInt("first_page");
		     last = cstmt.getInt("last_page");
            			 
		 } catch (SQLException e) {			
			 e.printStackTrace();
		 } catch (Exception e) {				
			 e.printStackTrace();	
		 } 

         
         // Get Product Filter
		 this.initProductFilter();
		 
		 return ProductResponse.builder()
				.totalPages(totalPages)
				.number(number)
				.size(size)
				.totalElements(totalElements)
				.totalPages(totalPages)
				.first(first)
				.last(last)
				.products(pList)
				.prdNames(pNames)
				.prdSources(pSources)
				.prdCats(pCats)
				.build();
	 }
	 
	 private void initProductFilter() {
		 
		List<Product> pList = new ArrayList<Product>();	
		
		 try (
		     Connection con = dataSource.getConnection();	 
		     CallableStatement cstmt = con.prepareCall("{call proc_product_filter()}"); 
		 ){	
			 ResultSet rs = cstmt.executeQuery();
		     
			 while (rs.next()) {
                Product pDto = Product.builder()
                    .productName(rs.getString("product_name"))
                    .categoryName(rs.getString("category_name"))
                    .sourceName(rs.getString("source_name"))
                    .build();
                
                pList.add(pDto);
             }
             
			 pNames = pList.stream()
	 	            .map(Product::productName) 
	 	            .distinct()                   
	 	            .collect(Collectors.toList()); 
	 		 
			 pSources = pList.stream()
			            .map(Product::sourceName)
			            .distinct()                    
			            .collect(Collectors.toList());
	 		 pCats = pList.stream()
			            .map(Product::categoryName) 
			            .distinct()                    
			            .collect(Collectors.toList());
		 		 
		 } catch (SQLException e) {			
			 e.printStackTrace();
		 } catch (Exception e) {				
			 e.printStackTrace();	
		 } 
	 }
	 
	 public List<ProductSerials> getProductSerialsByProductId(ProductSerilsRequest request){
		 List<ProductSerials> prdSerials = new ArrayList<>();
		 try (
			     Connection con = dataSource.getConnection();	 
			     CallableStatement cstmt = con.prepareCall("{call proc_product_serial_filter(?)}"); 
			 ){	
			     cstmt.setInt("p_product_id", request.productId());
				 ResultSet rs = cstmt.executeQuery();
			     
				 while (rs.next()) {
					 ProductSerials pDto = ProductSerials.builder()
	                    .productId(rs.getString("product_id"))
	                    .serialId(rs.getString("id"))
	                    .serialCode(rs.getString("serial_code"))
	                    .assetCode(rs.getString("asset_id"))
	                    .usedBy(rs.getString("used_by"))
	                    .color(rs.getString("color"))
	                    .label(rs.getString("label"))
	                    .note(rs.getString("noted"))
	                    .stock(rs.getString("stock"))
	                    .build();	                
	                prdSerials.add(pDto);
	             }

			 		 
			 } catch (SQLException e) {			
				 e.printStackTrace();
			 } catch (Exception e) {				
				 e.printStackTrace();	
			 } 
		 return prdSerials;
	 }
}
