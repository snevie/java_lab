package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Представление задачи
 * Основная информация: название, дедлайн, приоритет и теги
 */
public class Task implements Comparable<Task> {
    private static int nextId = 1; // Статический счетчик для автоматической нумерации задач
    
    private final int id;
    private String title;
    private LocalDate dueDate;
    private Priority priority;
    private Set<Tag> tags;
    
    /**
     * Структура задачи
     * @param title название 
     * @param dueDate дедлайн
     * @param priority приоритет
     */
    public Task(String title, LocalDate dueDate, Priority priority) {
        this.id = nextId++;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.tags = new HashSet<>();
    }
    
    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public Set<Tag> getTags() { return new HashSet<>(tags); }
    
    // Сеттеры
    public void setTitle(String title) { this.title = title; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public void addTag(String tagName) {
        tags.add(new Tag(tagName));
    }
    
    public void removeTag(String tagName) {
        tags.remove(new Tag(tagName));
    }
    
    // Проверка наличия тега
    public boolean hasTag(String tagName) {
        return tags.contains(new Tag(tagName));
    }
    
    // Сравнение задач по дедлайну
    @Override
    public int compareTo(Task other) {
        return this.dueDate.compareTo(other.dueDate);
    }
    
    /**
     * Формат: "1. Название | Приоритет: ... | Дедлайн: ... | Теги: ..."
     */
    @Override
    public String toString() {
        return String.format("%d. %s | Приоритет: %s | Дедлайн: %s | Теги: %s",
                id, title, priority.getDisplayName(), dueDate, tags);
    }
}