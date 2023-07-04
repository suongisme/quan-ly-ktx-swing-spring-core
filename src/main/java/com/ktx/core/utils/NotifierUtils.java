package com.ktx.core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@RequiredArgsConstructor
public class NotifierUtils {

    public static final int EXIST_OPTION = -1;

    private final SystemUtils systemUtils;

    public void success(String title, String message) {
        Icon icon = new ImageIcon(systemUtils.getSysImage("success.png"));
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public void error(String title, String message) {
        Icon icon = new ImageIcon(systemUtils.getSysImage("error.png"));
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, icon);
    }
    
    public int confirmNotInput(String title, String message) {
        int i = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (i == EXIST_OPTION) {
            return JOptionPane.NO_OPTION;
        }
        return i;
    }
}
