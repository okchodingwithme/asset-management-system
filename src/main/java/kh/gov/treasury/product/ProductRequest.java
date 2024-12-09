package kh.gov.treasury.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String category;
	private String source;
	private String qty;
	private float cost;
	private int page;
	private int size;
	
}
