package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс для работы с датами
 */
public class DateUtil {
    // Форматтер для преобразования дат в строки и обратно
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    // Парсит строку в объект LocalDate
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    // Форматирует объект LocalDate в строку
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
    
    // Проверяет валидность строки с датой
    public static boolean isValidDate(String dateString) {
        return parseDate(dateString) != null;
    }
}