package com.ktx.core.encoder.password;

import java.util.Map;
import java.util.Optional;

public class DelegatingPasswordEncoder implements PasswordEncoder {

    private Map<String, PasswordEncoder> passwordEncoderMap;
    private PasswordEncoder passwordEncoder;
    private String idForEncode;

    public DelegatingPasswordEncoder(String idForEncode, Map<String, PasswordEncoder> map) {
        this.passwordEncoderMap = map;
        this.idForEncode = idForEncode;
        this.passwordEncoder = map.get(idForEncode);
    }

    private PasswordEncoder getPasswordEncoder(String type) {
        return Optional.ofNullable(this.passwordEncoderMap.get(type))
                .orElse(this.passwordEncoderMap.get(this.idForEncode));
    }

    @Override
    public String encode(String rawPassword) {
        return this.idForEncode + this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        String type = this.extractIdForEncode(encodedPassword);
        String encodedPassword1 = this.extractEncodedPassword(encodedPassword);
        return this.getPasswordEncoder(type).matches(rawPassword, encodedPassword1);
    }

    private String extractEncodedPassword(String rawPassword) {
        int start = rawPassword.indexOf("}");
        return rawPassword.substring(start + 1);
    }

    private String extractIdForEncode(String rawPassword) {
        int last = rawPassword.indexOf("}");
        return rawPassword.substring(0, last + 1);
    }
}
