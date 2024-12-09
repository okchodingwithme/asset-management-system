package kh.gov.treasury.utils;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record ResultObject<T> (
		Boolean isSuccessful,
		Integer code,
		String message,
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	    LocalDateTime timestamp,
		T payload
	) {
}