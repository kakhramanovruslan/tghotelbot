package com.tgbot.HotelBot.service;

import com.tgbot.HotelBot.config.BotConfig;
import com.tgbot.HotelBot.models.Booking;
import com.tgbot.HotelBot.models.Room;
import com.tgbot.HotelBot.models.User;
import com.tgbot.HotelBot.repositories.BookingRepository;
import com.tgbot.HotelBot.repositories.RoomRepository;
import com.tgbot.HotelBot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot{

    private final Text text;
    private final BotConfig config;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    List<Integer> availableRooms;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private Map<Long, Map<String, String>> answers = new HashMap<>();


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();

            Pattern pattern = Pattern.compile("-- (\\d+) --");
            Matcher matcher = pattern.matcher(callbackData);
            if (matcher.find()) {
                String roomNumber = matcher.group(1);
                System.out.println("RoomNumber: "+roomNumber);
                Integer price_per_day = roomRepository.findPricePerDayByRoomNumber(Integer.parseInt(roomNumber));
                LocalDate sd = LocalDate.parse(answers.get(chatId).get("startDate"), formatter);
                LocalDate ed = LocalDate.parse(answers.get(chatId).get("endDate"), formatter);
                long daysBetween = Math.abs(sd.toEpochDay() - ed.toEpochDay());
                answers.get(chatId).put("roomNumber", roomNumber);
                answers.get(chatId).put("totalPrice", String.valueOf(daysBetween*price_per_day));
                handleCallbackQuery(chatId, "booking_start");
            }

            handleCallbackQuery(chatId, callbackData);
        }
        else if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getChat().getUserName();

            Map<String, String> innerMap = answers.get(chatId);
            if (innerMap == null) {
                innerMap = new HashMap<>();
                answers.put(chatId, innerMap);
            }

            registration(messageText, chatId, username);

            Pattern datePattern = Pattern.compile("^(startDate|endDate)\\s+\\d{2}\\.\\d{2}\\.\\d{2}$");
            if (datePattern.matcher(messageText).matches()) {
                String[] parts = messageText.split(" ");

                switch (String.valueOf(parts[0])){
                    case "startDate":
                        innerMap.put("startDate", parts[1]);
                        handleCallbackQuery(chatId, "end_date");
                        break;
                    case "endDate":
                        innerMap.put("endDate", parts[1]);
                        handleCallbackQuery(chatId, "capacity_button");
                        break;
                }
                System.out.println(answers);

            } else {
                switch (messageText) {

                    case "/start":
                        List<String> greetingButtons = List.of("Забронировать");
                        List<String> callbackButtons = List.of("book_button");
                        sendPhotoWithTextAndInlineKeyboard(chatId, "src/main/java/com/tgbot/HotelBot/images/hotel.jpg", text.GREETING_TEXT, greetingButtons, callbackButtons);
                        break;
                    case "/info":
                        sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/infoHotel.jpg", text.INFO_TEXT);
                        break;
                    case "/info_rooms":
                        sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/econom.jpg", text.INFO_ECONOM);
                        sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/standart.jpg", text.INFO_STANDART);
                        sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/lux.jpg", text.INFO_LUX);
                        break;
                    case "/commands":
                        sendMessage(chatId, text.COMMANDS_TEXT);
                }
            }
        }
    }

    private void handleCallbackQuery(Long chatId, String callbackData) throws ParseException {

        switch (callbackData) {

            case "book_button":
                if (!isRegister(chatId)) sendMessage(chatId, text.REGISTER_TEXT);
                else {
                    sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/checkIn.jpeg", text.START_DATE_TEXT);
                }
                break;

            case "end_date":
                sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/checkOut.jpg", text.END_DATE_TEXT);
                break;

            case "capacity_button":
                List<String> capacityButtons = List.of("1", "2", "3", "4");
                List<String> callbackButtons = List.of("capacity_1", "capacity_2", "capacity_3", "capacity_4");
                sendMessageWithInlineKeyboard(chatId, text.CAPACITY_TEXT, capacityButtons, callbackButtons);
                break;

            case "capacity_1":
                answers.get(chatId).put("capacity", "1");
                handleCallbackQuery(chatId, "category_button");
                break;

            case "capacity_2":
                answers.get(chatId).put("capacity", "2");
                handleCallbackQuery(chatId, "category_button");
                break;

            case "capacity_3":
                answers.get(chatId).put("capacity", "3");
                handleCallbackQuery(chatId, "category_button");
                break;

            case "capacity_4":
                answers.get(chatId).put("capacity", "4");
                handleCallbackQuery(chatId, "category_button");
                break;

            case "category_button":
                List<String> categoryButtons = List.of("Эконом", "Стандарт", "Люкс");
                List<String> сategoryCallbackButtons = List.of("econom", "standart", "lux");
                sendMessageWithInlineKeyboard(chatId, text.CATEGORY_TEXT, categoryButtons, сategoryCallbackButtons);
                break;

            case "econom":
                answers.get(chatId).put("category", "Эконом");
                handleCallbackQuery(chatId, "balcony");
                break;

            case "standart":
                answers.get(chatId).put("category", "Стандарт");
                handleCallbackQuery(chatId, "balcony");
                break;

            case "lux":
                answers.get(chatId).put("category", "Люкс");
                handleCallbackQuery(chatId, "balcony");
                break;

            case "balcony":
                List<String> balconyButtons = List.of("Да", "Нет");
                List<String> balconyCallbackButtons = List.of("balcony_yes", "balcony_no");
                sendMessageWithInlineKeyboard(chatId, text.BALCONY_TEXT, balconyButtons, balconyCallbackButtons);
                break;

            case "babyBed":
                List<String> babyBedButtons = List.of("Да", "Нет");
                List<String> babyBedCallbackButtons = List.of("babyBed_yes", "babyBed_no");
                sendMessageWithInlineKeyboard(chatId, text.BABY_BED_TEXT, babyBedButtons, babyBedCallbackButtons);
                break;

            case "smokingRoom":
                List<String> smokingRoomButtons = List.of("Да", "Нет");
                List<String> smokingRoomCallbackButtons = List.of("smokingRoom_yes", "smokingRoom_no");
                sendMessageWithInlineKeyboard(chatId, text.SMOKING_ROOM_TEXT, smokingRoomButtons, smokingRoomCallbackButtons);
                break;

            case "balcony_yes":
                answers.get(chatId).put("hasBalcony", "True");
                handleCallbackQuery(chatId, "babyBed");
                break;

            case "balcony_no":
                answers.get(chatId).put("hasBalcony", "False");
                handleCallbackQuery(chatId, "babyBed");
                break;

            case "babyBed_yes":
                answers.get(chatId).put("hasBabyBed", "True");
                handleCallbackQuery(chatId, "smokingRoom");
                break;

            case "babyBed_no":
                answers.get(chatId).put("hasBabyBed", "False");
                handleCallbackQuery(chatId, "smokingRoom");
                break;

            case "smokingRoom_yes":
                answers.get(chatId).put("smokingAllowed", "True");
                handleCallbackQuery(chatId, "find");
                System.out.println(answers);
                break;

            case "smokingRoom_no":
                answers.get(chatId).put("smokingAllowed", "False");
                handleCallbackQuery(chatId, "find");
                System.out.println(answers);
                break;

            case "find":
                this.availableRooms = roomRepository.findAvailableRoomNumbers(
                        answers.get(chatId).get("category"),
                        Integer.parseInt(answers.get(chatId).get("capacity")),
                        answers.get(chatId).get("hasBalcony").equals("True"),
                        answers.get(chatId).get("hasBabyBed").equals("True"),
                        answers.get(chatId).get("smokingAllowed").equals("True"),
                        LocalDateTime.of(LocalDate.parse(answers.get(chatId).get("startDate"), formatter), LocalTime.NOON),
                        LocalDateTime.of(LocalDate.parse(answers.get(chatId).get("endDate"), formatter), LocalTime.NOON)
                );
                handleCallbackQuery(chatId, "list_of_rooms");
                break;

            case "list_of_rooms":
                List<String> listRoomsButtons = this.availableRooms.stream().map(Object::toString).toList();
                List<String> listRoomsCallback = listRoomsButtons.stream().map(number -> "-- " + number + " --").collect(Collectors.toList());
                if (listRoomsButtons.isEmpty()) sendMessage(chatId, text.NO_ROOMS_TEXT);
                else sendMessageWithInlineKeyboard(chatId, text.LIST_OF_ROOMS, listRoomsButtons, listRoomsCallback);
                break;

            case "booking":
                Booking booking = new Booking();
                Optional<User> user = userRepository.findByChatId(chatId);
                Room room = roomRepository.findByRoomNumber(answers.get(chatId).get("roomNumber"));
                booking.setUser(user.get());
                booking.setRoom(room);
                booking.setTotalPrice(Integer.valueOf(answers.get(chatId).get("totalPrice")));
                booking.setCheckInDateTime(LocalDateTime.of(LocalDate.parse(answers.get(chatId).get("startDate"), formatter), LocalTime.NOON));
                booking.setCheckOutDateTime(LocalDateTime.of(LocalDate.parse(answers.get(chatId).get("endDate"), formatter), LocalTime.NOON));
                bookingRepository.save(booking);
                sendPhotoWithText(chatId, "src/main/java/com/tgbot/HotelBot/images/succesfullyBooking.jpg", text.END_TEXT);
                break;

            case "booking_start":
                List<String> finalBookButtons = List.of("Я согласен", "Отмена");
                List<String> finalBookCallbackButtons = List.of("booking", "/start");
                sendMessageWithInlineKeyboard(chatId, text.ACK_BOOK_TEXT+answers.get(chatId).get("totalPrice"), finalBookButtons, finalBookCallbackButtons);
                break;
        }
    }

    private boolean isRegister(Long chatId){
        Optional<User> user = userRepository.findByChatId(chatId);
        return user.isPresent();
    };


    private void sendMessageWithInlineKeyboard(long chatId, String text, List<String> buttons, List<String> callbackNames) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        if (buttons != null && !buttons.isEmpty() && callbackNames != null && !callbackNames.isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

            // Создаем ряды кнопок
            for (int i = 0; i < buttons.size(); i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttons.get(i));
                button.setCallbackData(callbackNames.get(i)); // Устанавливаем данные обратного вызова

                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(button);

                keyboardRows.add(row);
            }

            inlineKeyboardMarkup.setKeyboard(keyboardRows);
            message.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message sent rejected");
        }
    }



    private void sendMessage(long chatId, String text) {
        try{
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message sent rejected");
        }
    }

    private void registration(String messageText, Long chatId, String username) {
        Pattern pattern = Pattern.compile("^[\\p{L}]+ [\\p{L}]+ \\+?\\d+$");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches() && !isRegister(chatId)) {
            String[] parts = messageText.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            String phoneNumber = parts[2];

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setChatId(chatId);
            user.setUsername(username);

            userRepository.save(user);

        }
    }

    public void sendPhotoWithTextAndInlineKeyboard(Long chatId, String photoPath, String caption, List<String> buttonLabels, List<String> callbackData) {
        File photo = new File(photoPath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photo));
        sendPhoto.setCaption(caption);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (int i = 0; i < buttonLabels.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonLabels.get(i));
            button.setCallbackData(callbackData.get(i));
            row.add(button);
            rowsInline.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhotoWithText(Long chatId, String photoPath, String caption) {
        File photo = new File(photoPath);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photo));
        sendPhoto.setCaption(caption);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
