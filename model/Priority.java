package model;

/**
 * Перечисление приоритетов задач на основе матрицы Эйзенхауэра:
 * 1. Срочные и важные (сделать немедленно)
 * 2. Не срочные, но важные (запланировать)
 * 3. Срочные, но не важные (делегировать)
 * 4. Не срочные и не важные (удалять)
 */
public enum Priority {
    URGENT_IMPORTANT("Сделать немедленно"),
    NOT_URGENT_IMPORTANT("Запланировать"),
    URGENT_NOT_IMPORTANT("Делегировать"),
    NOT_URGENT_NOT_IMPORTANT("Удалить");
    
    private final String displayName; // Название приоритета
    
    Priority(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}