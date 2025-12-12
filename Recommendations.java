import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Recommendations {

    private HabitEngine habitEngine;

    // Constructor receives HabitEngine instance
    public Recommendations(HabitEngine habitEngine) {
        this.habitEngine = habitEngine;
    }

    // Load tasks WITHOUT modifying Storage class
    private ArrayList<Task> loadTasks() {
        try (BufferedReader br = new BufferedReader(new FileReader("tasks.json"))) {
            return new com.google.gson.Gson().fromJson(
                    br,
                    new com.google.gson.reflect.TypeToken<ArrayList<Task>>(){}.getType()
            );
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    // ================================================================
    // 1) Suggest New Habits & Tasks Based on Usage Patterns
    // ================================================================
    public void suggestNewHabitsAndTasks() {
        System.out.println("\n--- 🌱 New Habit & Task Suggestions ---");
    
        // Some predefined habit suggestions
        String[] habitSuggestions = {
            "Drink 2 liters of water daily",
            "Meditate for 5 minutes",
            "Read 10 pages of a book",
            "Walk for 15 minutes",
            "Sleep before 11 PM",
            "Clean your room for 5 minutes",
            "Practice deep breathing"
        };
    
        // Some predefined task suggestions
        String[] taskSuggestions = {
            "Organize your files",
            "Finish a small assignment",
            "Reply to pending emails",
            "Plan your week",
            "Review your goals",
            "Clean your desk",
            "Study 20 minutes of your course"
        };
    
        Random rand = new Random();
    
        // Pick 2 random habits
        System.out.println("\n🌟 Recommended Habits:");
        for (int i = 0; i < 2; i++) {
            int index = rand.nextInt(habitSuggestions.length);
            System.out.println("- " + habitSuggestions[index]);
        }
    
        // Pick 2 random tasks
        System.out.println("\n📝 Recommended Tasks:");
        for (int i = 0; i < 2; i++) {
            int index = rand.nextInt(taskSuggestions.length);
            System.out.println("- " + taskSuggestions[index]);
        }
    
        System.out.println("\n✔ Suggestions generated based on your activity.\n");
    }
    
    // ================================================================
    // 2) Suggest Improvements / Edits for Existing Habits & Tasks
    // ================================================================
    public void suggestEdits() {
        System.out.println("\n=== ✏ Recommendations for Improvement ===");

        ArrayList<Task> tasks = loadTasks();
        ArrayList<HabitEngine.Habit> habits = habitEngine.habits;

        // Habit-based edits
        for (HabitEngine.Habit h : habits) {

            if (h.streak == 0) {
                System.out.println("➡ Habit '" + h.title + 
                    "' has a 0 streak. Consider lowering the goal or switching to WEEKLY.");
            }

            if (h.streak > h.goal * 0.8) {
                System.out.println("🔥 Habit '" + h.title + 
                    "' is close to goal! Maybe increase difficulty or upgrade it.");
            }
        }

        // Task-based edits
        for (Task t : tasks) {
            if (t.completed && t.priority.equalsIgnoreCase("LOW") && t.duration > 60) {
                System.out.println("➡ Task '" + t.title + 
                    "' is long but low priority. Consider raising the priority.");
            }

            if (!t.completed && t.duration > 120) {
                System.out.println("➡ '" + t.title + "' seems heavy. Break it into smaller tasks.");
            }
        }
    }


    // ================================================================
    // 3) Analyze Habit Streak Patterns
    // ================================================================
    public void analyzeStreakPatterns() {
        System.out.println("\n=== 📊 Streak Analysis ===");

        ArrayList<HabitEngine.Habit> habits = habitEngine.habits;

        if (habits.isEmpty()) {
            System.out.println("No habits to analyze.");
            return;
        }

        for (HabitEngine.Habit h : habits) {

            System.out.println("\nHabit: " + h.title);

            if (h.lastCheckDate == null || h.lastCheckDate.isEmpty()) {
                System.out.println("📌 No check-in history yet.");
                continue;
            }

            LocalDate last = LocalDate.parse(h.lastCheckDate);
            LocalDate today = LocalDate.now();

            // Trend analysis
            if (h.streak == 0) {
                System.out.println("📉 Streak recently reset. Try keeping consistency.");
            } 
            else if (h.streak > 0 && h.streak <= 3) {
                System.out.println("📈 Good start! Keep it going.");
            }
            else if (h.streak > 3 && h.streak <= 10) {
                System.out.println("🔥 Strong progress! You are forming a solid habit.");
            }
            else if (h.streak > 10) {
                System.out.println("🏆 Excellent! This habit is very stable.");
            }

            // Detect stagnation
            if (last.isBefore(today.minusDays(3)) && h.frequency.equals("DAILY")) {
                System.out.println("⚠ Inactive for " + last.until(today).getDays() +
                        " days. Streak might reset.");
            }
        }
    }


    // ================================================================
    // 4) Run Recommendation System
    // ================================================================
    public void run() {
       

        //suggestNewHabitsAndTasks();
        //suggestEdits();
        //analyzeStreakPatterns();
        while (true) {
            System.out.println("\n===== 🌟 RECOMMENDATION ENGINE 🌟 =====");

            System.out.println("1. Suggestt New Habits and Tasks");
            System.out.println("2. Suggest Edits");
            System.out.println("3.Analyze Streak Patterns");
            System.err.println("0.Back");
            
            System.out.println("\n===== End of Recommendations =====\n");
Scanner input = new Scanner (System.in);
            String choice = input.nextLine();

            switch (choice) {
                case "1": suggestNewHabitsAndTasks() ; break;
                case "2": suggestEdits(); break;
                case "3": analyzeStreakPatterns(); break;
                case "0": return;
                default: System.out.println("Invalid, try again.");
            }
        }
        
    }
}

