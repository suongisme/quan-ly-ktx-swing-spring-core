package com.ktx.core.encoder.password;

import java.util.HashMap;
import java.util.Map;

public class DelegatePasswordEncoderFactory {

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String idForEncode = "{bcrypt}";
        Map<String, PasswordEncoder> passwordEncoderMap = new HashMap<>();
        passwordEncoderMap.put(idForEncode, new BcryptPasswordEncoder());
        passwordEncoderMap.put("{noop}", new NoopPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, passwordEncoderMap);
    }
}