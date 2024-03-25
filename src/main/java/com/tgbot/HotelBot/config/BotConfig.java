package com.tgbot.HotelBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("application.properties")
@Data
@Configuration
public class BotConfig {

    @Value(value = "${bot.name}")
    private String botName;

    @Value(value = "${bot.token}")
    private String token;
}
