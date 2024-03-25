package com.tgbot.HotelBot.service;

import com.tgbot.HotelBot.repositories.BookingRepository;
import com.tgbot.HotelBot.repositories.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Data
public class CommandHandler {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public void handleStartCommand(Long chatId) {
        // Обработка команды /start
    }

    public void handleTextMessage(Message message) {
        // Обработка текстовых сообщений
    }

    public void handleCallback(Long chatId, String callbackData) {
        // Обработка коллбэков от кнопок
    }
}