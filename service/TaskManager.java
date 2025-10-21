package service;

import model.Task;
import model.Priority;
import model.Tag;
import util.DateUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Основные операции: создание, редактирование, удаление, поиск и сортировка задач
 */
public class TaskManager {
    private final List<Task> tasks; // Список всех задач
    private final Scanner scanner;   // Сканер для ввода пользовательских данных
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    
    public void createTask() {
        System.out.println("\n=== Создание новой задачи ===");
        
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Ошибка: название задачи не может быть пустым");
            return;
        }
        
        LocalDate dueDate = readDate("Дедлайн (дд.мм.гггг): ");
        if (dueDate == null) return; // Пользователь прервал ввод
        
        Priority priority = readPriority();
        if (priority == null) return;
        
        Task task = new Task(title, dueDate, priority);
        addTagsToTask(task);
        
        tasks.add(task);
        System.out.println("Задача успешно создана");
    }
    
    public void editTask() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        
        System.out.println("\n=== Редактирование задачи ===");
        displayAllTasks();
        
        System.out.print("Введите номер задачи: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);
            
            if (task == null) {
                System.out.println("Задача не найдена");
                return;
            }
            
            boolean continueEditing = true;
            while (continueEditing) {
                System.out.println("\nРедактирование задачи:");
                System.out.println("1. Изменить название");
                System.out.println("2. Изменить дедлайн");
                System.out.println("3. Изменить приоритет");
                System.out.println("4. Добавить тег");
                System.out.println("5. Удалить тег");
                System.out.println("0. Завершить редактирование");
                
                System.out.print("Выберите действие: ");
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        System.out.print("Введите новое название: ");
                        String newTitle = scanner.nextLine().trim();
                        if (!newTitle.isEmpty()) {
                            task.setTitle(newTitle);
                            System.out.println("Название обновлено");
                        } else {
                            System.out.println("Название не может быть пустым");
                        }
                        break;
                    case "2":
                        LocalDate newDueDate = readDate("Введите новый дедлайн (дд.мм.гггг): ");
                        if (newDueDate != null) {
                            task.setDueDate(newDueDate);
                            System.out.println("Дата дедлайна обновлена");
                        }
                        break;
                    case "3":
                        Priority newPriority = readPriority();
                        if (newPriority != null) {
                            task.setPriority(newPriority);
                            System.out.println("Приоритет обновлен");
                        }
                        break;
                    case "4":
                        System.out.print("Введите тег для добавления: ");
                        String tagToAdd = scanner.nextLine().trim();
                        if (!tagToAdd.isEmpty()) {
                            task.addTag(tagToAdd);
                            System.out.println("Тег добавлен");
                        } else {
                            System.out.println("Тег не может быть пустым");
                        }
                        break;
                    case "5":
                        System.out.print("Введите тег для удаления: ");
                        String tagToRemove = scanner.nextLine().trim();
                        if (!tagToRemove.isEmpty()) {
                            task.removeTag(tagToRemove);
                            System.out.println("Тег удален");
                        } else {
                            System.out.println("Тег не может быть пустым");
                        }
                        break;
                    case "0":
                        continueEditing = false;
                        System.out.println("Редактирование завершено");
                        break;
                    default:
                        System.out.println("Неверный выбор");
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Номер задачи должен быть числом");
        }
    }
    
    public void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        
        System.out.println("\n=== Удаление задачи ===");
        displayAllTasks();
        
        System.out.print("Введите номер задачи для удаления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Task task = findTaskById(id);
            
            if (task != null) {
                tasks.remove(task);
                System.out.println("Задача успешно удалена");
            } else {
                System.out.println("Задача не найдена!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Номер задачи должен быть числом!");
        }
    }
    
    public void searchByTags() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        
        System.out.println("\n=== Поиск по тегам ===");
        System.out.print("Введите теги для поиска (через запятую): ");
        String tagsInput = scanner.nextLine().trim();
        
        if (tagsInput.isEmpty()) {
            System.out.println("Не введены теги для поиска");
            return;
        }
        
        // Разбиваем введенные теги
        Set<Tag> searchTags = Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Tag::new)
                .collect(Collectors.toSet());
        
        // Ищем задачи, содержащие хотя бы один из указанных тегов
        List<Task> foundTasks = tasks.stream()
                .filter(task -> task.getTags().stream().anyMatch(searchTags::contains))
                .collect(Collectors.toList());
        
        if (foundTasks.isEmpty()) {
            System.out.println("Задачи с указанными тегами не найдены");
        } else {
            System.out.println("\nНайденные задачи:");
            foundTasks.forEach(System.out::println);
        }
    }
   
    public void sortByDate() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.naturalOrder());
        
        System.out.println("\n=== Задачи, отсортированные по дате делайна ===");
        sortedTasks.forEach(System.out::println);
    }
    
    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        
        System.out.println("\n=== Список задач ===");
        tasks.forEach(System.out::println);
    }
    
    // Вспомогательные методы
    // Нахождение задачи по идентификатору
    private Task findTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    // Чтение и валидация даты
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String dateString = scanner.nextLine().trim();
            
            if (DateUtil.isValidDate(dateString)) {
                LocalDate date = DateUtil.parseDate(dateString);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Дата не может быть указана в прошлом");
                    continue;
                }
                return date;
            } else {
                System.out.println("Неверный формат даты. Используйте дд.мм.гггг");
                System.out.print("Повторить ввод? (y/n): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (!answer.equals("y")) {
                    return null;
                }
            }
        }
    }
    
    // Чтение и валидация приоритета
    private Priority readPriority() {
        while (true) {
            System.out.println("\nВыберите приоритет:");
            System.out.println("1 - " + Priority.URGENT_IMPORTANT.getDisplayName());
            System.out.println("2 - " + Priority.NOT_URGENT_IMPORTANT.getDisplayName());
            System.out.println("3 - " + Priority.URGENT_NOT_IMPORTANT.getDisplayName());
            System.out.println("4 - " + Priority.NOT_URGENT_NOT_IMPORTANT.getDisplayName());
            System.out.print("Ваш выбор: ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": return Priority.URGENT_IMPORTANT;
                case "2": return Priority.NOT_URGENT_IMPORTANT;
                case "3": return Priority.URGENT_NOT_IMPORTANT;
                case "4": return Priority.NOT_URGENT_NOT_IMPORTANT;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    System.out.print("Повторить ввод? (y/n): ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (!answer.equals("y")) {
                        return null;
                    }
            }
        }
    }
    
    // Добавление тега
    private void addTagsToTask(Task task) {
        System.out.println("Добавление тегов (для завершения введите пустую строку):");
        
        while (true) {
            System.out.print("Введите тег: ");
            String tag = scanner.nextLine().trim();
            
            if (tag.isEmpty()) {
                break;
            }
            
            task.addTag(tag);
            System.out.println("Тег добавлен");
        }
    }
}