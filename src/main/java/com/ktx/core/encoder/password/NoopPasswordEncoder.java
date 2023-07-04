package com.ktx.core.encoder.password;

import org.springframework.lang.NonNull;

public class NoopPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(@NonNull String rawPassword, @NonNull String encodedPassword) {
        return encodedPassword.equals(rawPassword);
    }
}
