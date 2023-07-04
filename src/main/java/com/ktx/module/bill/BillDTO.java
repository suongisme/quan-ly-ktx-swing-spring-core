package com.ktx.module.bill;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ktx.core.constant.PaymentEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BillDTO {
	
	private String code;
	
	@NotBlank(message = "Tháng không được để trống")
	@Pattern(regexp = "(1|2|3|4|5|6|7|8|9|10|11|12)", message = "Tháng không hợp lệ")
	private String month;
	
	@NotBlank(message = "Năm không được để trống")
	@Pattern(regexp = "[0-9]+", message = "Năm không hợp lệ")
	private String year;
	
	@NotBlank(message = "Số điện đã sử dụng không được để trống")
	@Pattern(regexp = "[0-9]+", message = "Số điện đã sử dụng không hợp lệ")
	private String total;
	
	@NotBlank(message = "Mã phòng không được trống")
	private String roomCode;
    
	@NotNull(message = "Trạng thái không được trống")
	private PaymentEnum status;

}
