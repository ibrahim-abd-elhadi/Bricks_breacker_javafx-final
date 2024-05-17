package org.example.bricks_breacker_final_project;
import java.io.*;

public class GameController {
    private GamePaneController gamePaneController;
    private final String SCORE_FILE = "C:\\Users\\alqay\\bricks_breacker_final_project\\src\\main\\resources\\org\\example\\bricks_breacker_final_project\\best_score.txt";

    public GameController(GamePaneController gamePaneController) {
        this.gamePaneController = gamePaneController;
    }

    public void loadBestScore() {
        File scoreFile = new File(SCORE_FILE);

        if (!scoreFile.exists()) {
            System.out.println("Score file does not exist. Creating a new file...");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scoreFile))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                gamePaneController.setBestScore(Integer.parseInt(line.trim()));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading best score from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveBestScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(Integer.toString(gamePaneController.getBestScore()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
