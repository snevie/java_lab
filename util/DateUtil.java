package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Утилитный класс для работы с датами
 */
public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    /**
     * Парсит строку в дату
     * @param dateString строка в формате dd.MM.yyyy
     * @return LocalDate или null при ошибке парсинга
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Форматирует дату в строку
     * @param date дата для форматирования
     * @return отформатированная строка
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Проверяет валидность даты
     * @param dateString строка для проверки
     * @return true если строка может быть преобразована в дату
     */
    public static boolean isValidDate(String dateString) {
        return parseDate(dateString) != null;
    }
}