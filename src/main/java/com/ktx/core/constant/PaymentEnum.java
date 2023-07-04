package com.ktx.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentEnum {
	PAID("Đã thành toán"),
	UN_PAID("Chưa thanh toán");
	
	private final String value;
}
