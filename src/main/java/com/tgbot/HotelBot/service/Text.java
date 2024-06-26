package com.tgbot.HotelBot.service;

import org.springframework.stereotype.Component;

@Component
public class Text {
    String GREETING_TEXT = "Добро пожаловать в наш отель!\n" +
            "Я ваш виртуальный бот для онлайн бронирования номеров.\n" +
            "Нажмите \"Забронировать\" и я помогу вам найти подходящий номер для отдыха.";

    String DEFAULT_TEXT = "Извините, я не понимаю ваш запрос. Пожалуйста, выберите что нибудь из перечисленного.";

    String REGISTER_TEXT = "К сожалению ваших данных нет в нашей базе, но не огорчайтесь, это легко исправить\nВведите данные в формате:\n" +
            "Фамилия Имя НомерТелефона, например: Иванов Иван 87772221420)\n" +
            "После авторизации снова введите команду /start чтобы начать бронирование";

    String START_DATE_TEXT = "Какая дата заезда вас интересует?\n" +
            "Напиши в формате \"startDate DD.MM.YY\"";

    String END_DATE_TEXT = "Какая дата выезда вас интересует?\n" +
            "Напиши в формате \"endDate DD.MM.YY\"";

    String CAPACITY_TEXT = "Сколько гостей будут проживать в номере?";

    String CATEGORY_TEXT = "Какой категории номер вы предпочитаете?";

    String BALCONY_TEXT = "Предпочитаете ли вы номер с балконом?";

    String BABY_BED_TEXT = "Требуется ли вам детская кроватка?";

    String SMOKING_ROOM_TEXT = "Желаете ли вы забронировать номер для курящих?";

    String LIST_OF_ROOMS = "Перед вами список свободных номеров, где первая цифра означает этаж.\n" +
            "Нажмите для бронирования";

    String NO_ROOMS_TEXT = "К сожалению, свободных номеров по вашим требованиям нет\n" +
            "Попробуйте начать заново и изменить требования, для этого введите /start";

    String END_TEXT = "Номер успешно забронирован, ждем вашего заезда! Предупреждаем что оплата производится во время заезда.";

    String ACK_BOOK_TEXT = "Цена за проживание: ";

    String INFO_TEXT = "Добро пожаловать в наш отель!\n" +
            "\n" +
            "Расположение: г. Москва, ул. Примерная, д. 123\n" +
            "Телефон: +7 123 456-78-90\n" +
            "Электронная почта: info@hotel.ru\n" +
            "\n" +
            "Услуги:\n" +
            "- Одноместные, двухместные и семейные номера\n" +
            "- Бесплатный Wi-Fi\n" +
            "- Ресторан с разнообразным меню\n" +
            "- Конференц-залы для проведения мероприятий\n" +
            "- Фитнес-центр и бассейн\n" +
            "- Парковка для гостей\n" +
            "\n" +
            "Мы гарантируем комфортное пребывание и качественное обслуживание нашим гостям!";

    String INFO_ECONOM = "Номер Эконом:\n" +
            "\n" +
            "Площадь: уютные 15-20 кв.м.\n" +
            "Стиль: минимализм с функциональностью\n" +
            "Удобства:\n" +
            "Уютная односпальная или двуспальная кровать\n" +
            "Компактная ванная комната с душем\n" +
            "Бесплатный Wi-Fi для связи с миром\n" +
            "Особенности:\n" +
            "Идеально подходит для путешественников с ограниченным бюджетом\n" +
            "Все необходимое для комфортного отдыха и ночлега";

    String INFO_STANDART = "Номер Стандарт:\n" +
            "\n" +
            "Площадь: просторные 20-25 кв.м.\n" +
            "Стиль: современный дизайн с уютом\n" +
            "Удобства:\n" +
            "Удобная двуспальная кровать или две односпальные кровати\n" +
            "Ванная комната с душем или ванной\n" +
            "Телевизор, мини-холодильник для удобства гостей\n" +
            "Особенности:\n" +
            "Подходит для пар и семейного отдыха\n" +
            "Больше пространства и удобств для приятного отдыха";

    String INFO_LUX = "Номер Люкс:\n" +
            "\n" +
            "Площадь: роскошные 30-40 кв.м.\n" +
            "Стиль: элегантный и роскошный интерьер\n" +
            "Удобства:\n" +
            "Кровать king-size, гостиная зона для отдыха\n" +
            "Просторная ванная комната с ванной и душем\n" +
            "Телевизоры в спальне и гостиной, мини-бар для удовольствия гостей\n" +
            "Особенности:\n" +
            "Предоставляет роскошный и уютный отдых\n" +
            "Идеальный выбор для особых случаев и романтических встреч";

    String COMMANDS_TEXT = "/start: Нажмите кнопку \"start\", чтобы начать процесс бронирования номера.\n" +
            "\n" +
            "/info: Узнайте больше о нашем отеле, его расположении, контактных данных и основных удобствах.\n" +
            "\n" +
            "/info_rooms: Информация о различных типах номеров: Эконом, Стандарт и Люкс. Узнайте, какой номер подойдет именно вам!\n" +
            "\n" +
            "/commands: Получите список доступных команд. Это поможет вам узнать, что можете делать с нашим ботом: получать информацию, бронировать номера и многое другое.";

}