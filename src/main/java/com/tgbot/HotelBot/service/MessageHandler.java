package com.tgbot.HotelBot.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Data
public class MessageHandler {

    private final CommandHandler commandHandler;

    public void handleMessage(Message message) {
        String text = message.getText();
        if (text != null) {
            if (text.startsWith("/start")) {
                // Обработка команды /start
                commandHandler.handleStartCommand(message.getChatId());
            } else {
                // Обработка других текстовых сообщений
                commandHandler.handleTextMessage(message);
            }
        }
    }
}