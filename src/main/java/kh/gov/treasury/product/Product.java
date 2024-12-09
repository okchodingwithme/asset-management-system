package kh.gov.treasury.product;

import lombok.Builder;

@Builder
public record Product(
	String productId,
	String productName,
	String deliveryDate,
	String categoryName,
	String sourceName,
	String quantity,
	String remain,
	String currency,
	String cost,
	String image
) {

}
