package com.ktx.module.room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoomDTO {
	
	@NotBlank(message = "Mã phòng không được trống")
	private String code;
	
	@NotBlank(message = "Tên phòng không được trống")
	private String name;
	
	@NotBlank(message = "Giá phòng không được trống")
	@Pattern(regexp = "^[1-9][0-9]*", message = "Giá phòng không hợp lệ")
	private String price;
	
	@NotBlank(message = "Số sinh viên/phòng không được trống")
	@Pattern(regexp = "^[1-9][0-9]*", message = "Số sinh viên/phòng không hợp lệ")
	private String maxPeople;
}
