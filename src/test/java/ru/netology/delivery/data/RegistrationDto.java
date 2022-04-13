package ru.netology.delivery.data;

import lombok.Value;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RegistrationDto {

    private RegistrationDto() {
    }

    private static final String[] cities = new String[]{"Москва", "Краснодар", "Казань", "Майкоп",
            "Новосибирск", "Калининград", "Санкт-Петербург", "Владивосток", "Хабаровск", "Пермь",
            "Красноярск", "Петропавловск-Камчатский", "Астрахань", "Самара", "Оренбург", "Курск"};

    @Value
    public static class RegistrationInfo {
        private String city;
        private String name;
        private String phone;
    }

    private static Faker faker;

    public static RegistrationInfo getRegistrationInfo() {

        faker = new Faker(new Locale("ru"));

        Random random = new Random();
        String city = cities[random.nextInt(cities.length)];
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();
        return new RegistrationInfo(city, name, phone);
    }


    public static String getDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

};
