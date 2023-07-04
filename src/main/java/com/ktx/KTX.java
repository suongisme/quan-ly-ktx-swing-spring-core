package com.ktx;

import com.ktx.core.config.RootConfig;
import com.ktx.module.auth.LoginView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class KTX {

    public static void main(String[] args) {
        setupTheme();
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
        LoginView loginView = context.getBean(LoginView.class);
        loginView.setVisible(true);
    }
    
    private static void setupTheme() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
