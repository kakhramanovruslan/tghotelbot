package com.tgbot.HotelBot.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public static String formatDate(String dateString) {
        try {
            // Парсинг даты из строки
            Date date = dateFormat.parse(dateString);

            // Проверка на валидность даты
            if (isValidDate(date)) {
                // Форматирование даты в строку
                return dateFormat.format(date);
            } else {
                return "Invalid date format!";
            }
        } catch (ParseException e) {
            return "Invalid date format!";
        }
    }

    private static boolean isValidDate(Date date) {
        try {
            // Проверка на валидность даты
            dateFormat.setLenient(false);
            dateFormat.parse(dateFormat.format(date));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}