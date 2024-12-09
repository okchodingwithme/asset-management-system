package kh.gov.treasury.product;

import lombok.Builder;

@Builder
public record ProductSerials(
		String serialId,
		String productId,
		String serialCode,
		String assetCode,
		String stock,
		String usedBy,
		String status,
		String color,
		String label,
		String note
		) {

}
