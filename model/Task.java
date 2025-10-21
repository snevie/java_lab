package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс для представления задачи
 */
public class Task implements Comparable<Task> {
    private static int nextId = 1;
    
    private final int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;
    private Set<Tag> tags;
    
    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
        this.tags = new HashSet<>();
    }
    
    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public Set<Tag> getTags() { return new HashSet<>(tags); }
    
    // Сеттеры
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    // Методы для работы с тегами
    public void addTag(String tagName) {
        tags.add(new Tag(tagName));
    }
    
    public void removeTag(String tagName) {
        tags.remove(new Tag(tagName));
    }
    
    public boolean hasTag(String tagName) {
        return tags.contains(new Tag(tagName));
    }
    
    @Override
    public int compareTo(Task other) {
        return this.dueDate.compareTo(other.dueDate);
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s | Приоритет: %s | Срок: %s | Теги: %s | %s",
                id, title, priority.getDisplayName(), dueDate, tags,
                completed ? "✓ Выполнена" : "⏳ В процессе");
    }
}