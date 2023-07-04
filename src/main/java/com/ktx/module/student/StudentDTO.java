package com.ktx.module.student;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class StudentDTO {

    @NotBlank(message = "Mã SV không được trống")
    private String studentId;

    @NotBlank(message = "Tên SV không được trống")
    private String name;

    @NotBlank(message = "SĐT không được trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}", message = "SĐT không hợp lệ")
    private String phone;

    @NotBlank(message = "Địa chỉ không được trống")
    private String address;

    @NotBlank(message = "Ngày sinh không được trống")
    private String dob;

    @NotBlank(message = "Mã phòng không được để trống")
    private String roomCode;
}
