package com.ktx.core.config;

import com.ktx.core.encoder.password.DelegatePasswordEncoderFactory;
import com.ktx.core.encoder.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
@ComponentScan(basePackages = "com.ktx")
@Import(JpaConfig.class)
public class RootConfig {

    @Bean
    public SmartValidator smartValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return DelegatePasswordEncoderFactory.createDelegatingPasswordEncoder();
    }
}
