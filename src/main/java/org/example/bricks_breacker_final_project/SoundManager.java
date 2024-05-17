package org.example.javafx_project_bricksbreaker;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundManager {
    static void playSound(String soundFilePath) {
        // Create a new MediaPlayer instance for each hit sound
        Media media = new Media(SoundManager.class.getResource(soundFilePath).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();  // Play the sound effect
        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose); // Dispose after playing
    }
}


