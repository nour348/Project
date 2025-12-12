package noursalem;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MoodEngine {

    private Scanner input = new Scanner(System.in);

    // Hashtable to store moods (date → mood)
    private Hashtable<String, String> moodHistory;

    // References to the other modules
    private TaskManager taskManager;
    private HabitEngine habitEngine;

    // JSON storage
    private static final String moodFile = "moods.json";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Constructor
    public MoodEngine(TaskManager tm, HabitEngine he) {
        this.taskManager = tm;
        this.habitEngine = he;
        this.moodHistory = loadMoodHistory();  // Load from JSON
    }

    // ========================================================
    // MAIN MOOD MENU
    // ========================================================
    public void moodMenu() {
        while (true) {
            System.out.println("\n=== *Mood Analysis* ===");
            System.out.println("1. Add Today's Mood");
            System.out.println("2. Analyze Mood");
            System.out.println("3. Suggest Tasks for Mood");
            System.out.println("4. Suggest habit for Mood");
            System.out.println("5. View Mood History");
            System.out.println("6. Back");


            String choice = input.nextLine();

            switch (choice) {
                case "1": addMood(); break;
                case "2": analyzeMood(); break;
                case "3": suggestTaskBasedOnMood(); break;
                case "4": suggestHabitBasedOnMood(); break;
                case "5": viewMoodHistory(); break;
                case "6": saveMoodHistory(); return;  // Save on exit
                default: System.out.println("Invalid, try again.");
            }
        }
    }

    // ========================================================
    // 1. ADD MOOD
    // ========================================================
    public void addMood() {
        System.out.println("\nHow are you feeling today?");
        System.out.println("1. Happy");
        System.out.println("2. Good");
        System.out.println("3. Neutral");
        System.out.println("4. Sad");
        System.out.println("5. Stressed");
        System.out.println("6. Awful");

        String today = LocalDate.now().toString();
        int c = Integer.parseInt(input.nextLine());
        String mood = convertMood(c);

        if (mood == null) {
            System.out.println("Invalid mood.");
            return;
        }

        moodHistory.put(today, mood);

        // Auto-adjust everything
        autoAdjustHabits(mood);
        autoAdjustTasks(mood);

        System.out.println("Mood saved for " + today + ": " + mood);
        saveMoodHistory(); // Save immediately
    }

    // Converts number to mood using switch
    private String convertMood(int c) {
        switch (c) {
            case 1: return "HAPPY";
            case 2: return "GOOD";
            case 3: return "NEUTRAL";
            case 4: return "SAD";
            case 5: return "STRESSED";
            case 6: return "AWFUL";
            default: return null;
        }
    }

    // ========================================================
    // 2. ANALYZE MOOD
    // ========================================================
    public void analyzeMood() {
        if (moodHistory.isEmpty()) {
            System.out.println("No mood data yet.");
            return;
        }

        int happy = 0, good = 0, neutral = 0, sad = 0, stressed = 0, awful = 0;

        for (String mood : moodHistory.values()) {
            switch (mood) {
                case "HAPPY": happy++; break;
                case "GOOD": good++; break;
                case "NEUTRAL": neutral++; break;
                case "SAD": sad++; break;
                case "STRESSED": stressed++; break;
                case "AWFUL": awful++; break;
            }
        }

        System.out.println("\n=== Your Mood Stats ===");
        System.out.println("Happy: " + happy);
        System.out.println("Good: " + good);
        System.out.println("Neutral: " + neutral);
        System.out.println("Sad: " + sad);
        System.out.println("Stressed: " + stressed);
        System.out.println("Awful: " + awful);
    }

    // ========================================================
    // 3. SUGGEST TASKS BASED ON MOOD
    // ========================================================
    public void suggestTaskBasedOnMood() {

        String today = LocalDate.now().toString();

        if (!moodHistory.containsKey(today)) {
            System.out.println("Add your mood first.");
            return;
        }

        String mood = moodHistory.get(today);

        System.out.println("\nBased on your mood (" + mood + "):");

        switch (mood) {
            case "HAPPY":
                System.out.println("- You can take on high priority tasks!");
                break;
            case "GOOD":
                System.out.println("- Medium-difficulty tasks fit you today.");
                break;
            case "NEUTRAL":
                System.out.println("- Try a small easy task.");
                break;
            case "SAD":
                System.out.println("- Do something calm or repetitive.");
                break;
            case "STRESSED":
                System.out.println("- Avoid big tasks. Try something relaxing.");
                break;
            case "AWFUL":
                System.out.println("- No tasks today, rest ❤️");
                break;
        }
    }

        // ========================================================
        // 5. SUGGEST HABITS BASED ON MOOD
        // ========================================================
     public void suggestHabitBasedOnMood() {
    	 
       String today = LocalDate.now().toString();

     if (!moodHistory.containsKey(today)) {
         System.out.println("Add your mood first.");
         return;
     }

     String mood = moodHistory.get(today);

     System.out.println("\nBased on your mood (" + mood + "), suggested habits:");

     if (habitEngine.habits == null || habitEngine.habits.isEmpty()) {
         System.out.println("No habits available to suggest.");
         return;
     }

     for (HabitEngine.Habit h : habitEngine.habits) {
         switch (mood) {
             case "HAPPY":
                 // Suggest challenging habits
                 if (h.streak < h.goal) 
                     System.out.println("- Try to push forward with: " + h.title);
                 break;
             case "GOOD":
                 // Suggest regular habits
                 System.out.println("- Keep up with: " + h.title);
                 break;
             case "NEUTRAL":
                 // Suggest light habits
                 System.out.println("- Easy habit to do today: " + h.title);
                 break;
             case "SAD":
                 // Suggest calming habits
                 if (h.frequency.equals("DAILY"))
                     System.out.println("- Calm daily habit: " + h.title);
                 break;
             case "STRESSED":
             case "AWFUL":
                 // Suggest low-pressure habits
                 System.out.println("- Do something light: " + h.title);
                 break;
         }
     }
 }


    // ========================================================
    // 4. VIEW MOOD HISTORY
    // ========================================================
    public void viewMoodHistory() {
        if (moodHistory.isEmpty()) {
            System.out.println("No mood entries yet.");
            return;
        }

        System.out.println("\n=== Mood History ===");
        for (String date : moodHistory.keySet()) {
            System.out.println(date + " → " + moodHistory.get(date));
        }
    }

    // ========================================================
    // AUTO-ADJUST HABITS BASED ON MOOD
    // ========================================================
    private void autoAdjustHabits(String mood) {

        if (habitEngine.habits == null || habitEngine.habits.isEmpty())
            return;

        for (HabitEngine.Habit h : habitEngine.habits) {
            switch (mood) {
                case "HAPPY": h.goal += 1; break;
                case "STRESSED":
                case "AWFUL": if (h.goal > 1) h.goal -= 1; break;
            }
        }

        HabitEngine.HabitStorage.saveHabits(habitEngine.habits);
    }

    // ========================================================
    // AUTO-ADJUST TASKS BASED ON MOOD
    // ========================================================
    private void autoAdjustTasks(String mood) {

        for (Task t : taskManager.tasks) {
            switch (mood) {
                case "HAPPY":
                    if (t.priority.equals("MED")) t.priority = "HIGH";
                    break;
                case "STRESSED":
                case "AWFUL":
                    if (t.priority.equals("HIGH")) t.priority = "MED";
                    break;
            }
        }

        Storage.savetasks(taskManager.tasks);
    }

    // ========================================================
    // SAVE/LOAD MOOD HISTORY TO JSON
    // ========================================================
    private void saveMoodHistory() {
        try (FileWriter fw = new FileWriter(moodFile)) {
            gson.toJson(moodHistory, fw);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private Hashtable<String, String> loadMoodHistory() {
        Hashtable<String, String> loaded = new Hashtable<>();
        try (BufferedReader br = new BufferedReader(new FileReader(moodFile))) {
            loaded = gson.fromJson(br, new TypeToken<Hashtable<String, String>>(){}.getType());
        } catch (Exception e) {
            // file may not exist yet → no problem
        }
        return loaded == null ? new Hashtable<>() : loaded;
    }
}

