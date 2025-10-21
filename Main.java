import service.TaskManager;
import java.util.Scanner;

/**
 * Главный класс приложения To-Do List
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();
    
    public static void main(String[] args) {
        System.out.println("=== To-Do List Manager ===");
        displayMenu();
        
        boolean running = true;
        while (running) {
            System.out.print("\nВыберите действие: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    taskManager.createTask();
                    break;
                case "2":
                    taskManager.displayAllTasks();
                    break;
                case "3":
                    taskManager.editTask();
                    break;
                case "4":
                    taskManager.deleteTask();
                    break;
                case "5":
                    taskManager.sortByDate();
                    break;
                case "6":
                    taskManager.searchByTags();
                    break;
                case "0":
                    running = false;
                    System.out.println("До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
                    displayMenu();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Отображает главное меню приложения
     */
    private static void displayMenu() {
        System.out.println("\n=== Главное меню ===");
        System.out.println("1. Создать новую задачу");
        System.out.println("2. Показать все задачи");
        System.out.println("3. Редактировать задачу");
        System.out.println("4. Удалить задачу");
        System.out.println("5. Сортировать задачи по дате");
        System.out.println("6. Поиск задач по тегам");
        System.out.println("0. Выход");
    }
}