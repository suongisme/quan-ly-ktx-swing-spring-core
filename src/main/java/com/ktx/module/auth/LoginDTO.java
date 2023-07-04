package com.ktx.module.auth;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class LoginDTO {

    @NotBlank(message = "Tên đăng nhập không được trống")
    @Size(min = 8, message = "Tên đăng nhập tối thiểu 8 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được trống")
    @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
    private String password;
}
