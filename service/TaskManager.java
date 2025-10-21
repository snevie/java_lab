package service;

import model.Task;
import model.Priority;
import model.Tag;
import util.DateUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для управления задачами
 */
public class TaskManager {
    private final List<Task> tasks;
    private final Scanner scanner;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Создает новую задачу
     */
    public void createTask() {
        System.out.println("\n=== Создание новой задачи ===");
        
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Ошибка: название задачи не может быть пустым!");
            return;
        }
        
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine().trim();
        
        LocalDate dueDate = readDate("Введите срок выполнения (дд.мм.гггг): ");
        if (dueDate == null) return;
        
        Priority priority = readPriority();
        if (priority == null) return;
        
        Task task = new Task(title, description, dueDate, priority);
        
        // Добавление тегов
        addTagsToTask(task);
        
        tasks.add(task);
        System.out.println("✅ Задача успешно создана!");
    }
    
    /**
     * Редактирует существующую задачу
     */
    public void editTask() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
            return;
        }
        
        System.out.println("\n=== Редактирование задачи ===");
        displayAllTasks();
        
        System.out.print("Введите ID задачи для редактирования: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);
            
            if (task == null) {
                System.out.println("Задача с ID " + id + " не найдена!");
                return;
            }
            
            boolean continueEditing = true;
            while (continueEditing) {
                System.out.println("\nРедактирование задачи:");
                System.out.println("1. Изменить название");
                System.out.println("2. Изменить описание");
                System.out.println("3. Изменить срок выполнения");
                System.out.println("4. Изменить приоритет");
                System.out.println("5. Добавить тег");
                System.out.println("6. Удалить тег");
                System.out.println("7. Отметить как выполненную/не выполненную");
                System.out.println("0. Завершить редактирование");
                
                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        System.out.print("Введите новое название: ");
                        String newTitle = scanner.nextLine().trim();
                        if (!newTitle.isEmpty()) {
                            task.setTitle(newTitle);
                            System.out.println("Название обновлено!");
                        }
                        break;
                    case "2":
                        System.out.print("Введите новое описание: ");
                        task.setDescription(scanner.nextLine().trim());
                        System.out.println("Описание обновлено!");
                        break;
                    case "3":
                        LocalDate newDueDate = readDate("Введите новый срок (дд.мм.гггг): ");
                        if (newDueDate != null) {
                            task.setDueDate(newDueDate);
                            System.out.println("Срок выполнения обновлен!");
                        }
                        break;
                    case "4":
                        Priority newPriority = readPriority();
                        if (newPriority != null) {
                            task.setPriority(newPriority);
                            System.out.println("Приоритет обновлен!");
                        }
                        break;
                    case "5":
                        System.out.print("Введите тег для добавления: ");
                        String tagToAdd = scanner.nextLine().trim();
                        if (!tagToAdd.isEmpty()) {
                            task.addTag(tagToAdd);
                            System.out.println("Тег добавлен!");
                        }
                        break;
                    case "6":
                        System.out.print("Введите тег для удаления: ");
                        String tagToRemove = scanner.nextLine().trim();
                        if (!tagToRemove.isEmpty()) {
                            task.removeTag(tagToRemove);
                            System.out.println("Тег удален!");
                        }
                        break;
                    case "7":
                        task.setCompleted(!task.isCompleted());
                        System.out.println("Статус задачи изменен на: " + 
                            (task.isCompleted() ? "Выполнена" : "В процессе"));
                        break;
                    case "0":
                        continueEditing = false;
                        System.out.println("Редактирование завершено!");
                        break;
                    default:
                        System.out.println("Неверный выбор!");
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом!");
        }
    }
    
    /**
     * Удаляет задачу
     */
    public void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
            return;
        }
        
        System.out.println("\n=== Удаление задачи ===");
        displayAllTasks();
        
        System.out.print("Введите ID задачи для удаления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);
            
            if (task != null) {
                tasks.remove(task);
                System.out.println("✅ Задача успешно удалена!");
            } else {
                System.out.println("Задача с ID " + id + " не найдена!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом!");
        }
    }
    
    /**
     * Выполняет поиск задач по тегам
     */
    public void searchByTags() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
            return;
        }
        
        System.out.println("\n=== Поиск по тегам ===");
        System.out.print("Введите теги для поиска (через запятую): ");
        String tagsInput = scanner.nextLine().trim();
        
        if (tagsInput.isEmpty()) {
            System.out.println("Не введены теги для поиска!");
            return;
        }
        
        Set<Tag> searchTags = Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Tag::new)
                .collect(Collectors.toSet());
        
        List<Task> foundTasks = tasks.stream()
                .filter(task -> task.getTags().stream().anyMatch(searchTags::contains))
                .collect(Collectors.toList());
        
        if (foundTasks.isEmpty()) {
            System.out.println("Задачи с указанными тегами не найдены!");
        } else {
            System.out.println("\nНайденные задачи:");
            foundTasks.forEach(System.out::println);
        }
    }
    
    /**
     * Сортирует задачи по дате выполнения
     */
    public void sortByDate() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
            return;
        }
        
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.naturalOrder());
        
        System.out.println("\n=== Задачи отсортированные по дате ===");
        sortedTasks.forEach(System.out::println);
    }
    
    /**
     * Отображает все задачи
     */
    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
            return;
        }
        
        System.out.println("\n=== Все задачи ===");
        tasks.forEach(System.out::println);
    }
    
    // Вспомогательные методы
    
    private Task findTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateString = scanner.nextLine().trim();
            
            if (DateUtil.isValidDate(dateString)) {
                LocalDate date = DateUtil.parseDate(dateString);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Ошибка: дата не может быть в прошлом!");
                    continue;
                }
                return date;
            } else {
                System.out.println("Ошибка: неверный формат даты! Используйте дд.мм.гггг");
                System.out.print("Хотите повторить ввод? (y/n): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (!answer.equals("y")) {
                    return null;
                }
            }
        }
    }
    
    private Priority readPriority() {
        while (true) {
            System.out.println("Выберите приоритет:");
            System.out.println("1 - Низкий");
            System.out.println("2 - Средний");
            System.out.println("3 - Высокий");
            System.out.print("Ваш выбор: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
                    System.out.print("Хотите повторить ввод? (y/n): ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (!answer.equals("y")) {
                        return null;
                    }
            }
        }
    }
    
    private void addTagsToTask(Task task) {
        System.out.println("Добавление тегов (для завершения введите пустую строку):");
        
        while (true) {
            System.out.print("Введите тег: ");
            String tag = scanner.nextLine().trim();
            
            if (tag.isEmpty()) {
                break;
            }
            
            task.addTag(tag);
            System.out.println("Тег '" + tag + "' добавлен!");
        }
    }
}