package com.ktx.core.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtils {

    public static final String DD_MM_YYYY = "dd/MM/yyyy";

    public String dateToString(Date date) {
        return this.dateToString(date, DD_MM_YYYY);
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public Date stringToDate(String date) {
        return this.stringToDate(date, DD_MM_YYYY);
    }

    public Date stringToDate(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(date);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
