package com.tgbot.HotelBot.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Data
public class UpdateHandler {

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    public void handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler.handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handleMessage(update.getMessage());
        }
    }
}
