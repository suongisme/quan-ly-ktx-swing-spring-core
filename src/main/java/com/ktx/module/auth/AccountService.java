package com.ktx.module.auth;

import com.ktx.core.constant.CacheEnum;
import com.ktx.core.encoder.password.PasswordEncoder;
import com.ktx.core.utils.CacheUtils;
import com.ktx.core.utils.ValidatorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private static final Long EXPIRED_SESSION = 1000L * 60 * 30; // 30 minutes
    private final AccountRepository accountRepository;
    private final CacheUtils cacheUtils;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtils validatorUtils;

    public void login(LoginDTO loginDTO) {
        log.info("login....");
    	this.validatorUtils.validate(loginDTO);
        Account account = this.accountRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác!"));
        if (!account.getActive()) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa!");
        }
        if (!this.passwordEncoder.matches(loginDTO.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }
        cacheUtils.set(CacheEnum.AUTHENTICATION, EXPIRED_SESSION, account);
    }

    public void logout() {
        cacheUtils.remove(CacheEnum.AUTHENTICATION);
    }

}
