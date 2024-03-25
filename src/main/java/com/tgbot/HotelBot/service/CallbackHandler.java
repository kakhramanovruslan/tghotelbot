package com.tgbot.HotelBot.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@Data
public class CallbackHandler {

    private final CommandHandler commandHandler;

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        commandHandler.handleCallback(chatId, callbackData);
    }
}