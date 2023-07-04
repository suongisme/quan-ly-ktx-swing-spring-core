package com.ktx.core.encoder.password;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.lang.NonNull;

public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(@NonNull String rawPassword) {
        return BCrypt.withDefaults().hashToString(6, rawPassword.toCharArray());
    }

    @Override
    public boolean matches(@NonNull String rawPassword, @NonNull String encodedPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified;
    }
}
