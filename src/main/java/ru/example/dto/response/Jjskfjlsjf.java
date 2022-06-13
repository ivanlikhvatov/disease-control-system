package ru.example.dto.response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Jjskfjlsjf {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");

        System.out.println(formatter.format(date));
    }
}
