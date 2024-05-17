package org.example.bricks_breacker_final_project;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    static void playSound(String soundFilePath) {
        try {
            // Create a new MediaPlayer instance for each hit sound
            Media media = new Media(SoundManager.class.getResource("src/main/resources/Sound_Effects/" + soundFilePath).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnError(() -> System.out.println("Media error: " + mediaPlayer.getError()));

            mediaPlayer.play();  // Play the sound effect
            mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose); // Dispose after playing
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
