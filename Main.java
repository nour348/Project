import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Load previously saved habits
        ArrayList<HabitEngine.Habit> habitList = HabitEngine.HabitStorage.loadHabits();
        HabitEngine habits = new HabitEngine(habitList);

        // Load Task Manager
        TaskManager tasks = new TaskManager(habits);

        // Load Mood Engine
        MoodEngine moodEngine = new MoodEngine(tasks, habits);

        // Load Recommendation Engine
        Recommendations recommendationEngine = new Recommendations(habits);

        while (true) {
            System.out.println("\n=== MAIN APP ===");
            System.out.println("1. Task Manager");
            System.out.println("2. Habit Engine");
            System.out.println("3. What is your mood today?");
            System.out.println("4. Recommendations");
            System.out.println("0. Exit");

            System.out.print("Choose: ");
            int choice = input.nextInt();
            input.nextLine();  // consume leftover newline

            switch (choice) {
                case 1:
                    tasks.run();
                    break;

                case 2:
                    habits.habitMenu();
                    break;

                case 3:
                    moodEngine.moodMenu();
                    break;

                case 4:
                    recommendationEngine.run();   // <-- Your recommendation menu
                    break;

                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

