package org.example.bricks_breacker_final_project;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    // In SoundManager
    public static void playSound(String soundFileName) {
        try {
            URL soundUrl = SoundManager.class.getResource( soundFileName);
            if (soundUrl == null) {
                System.out.println("Sound file not found: " + soundFileName);
                return;
            }
            System.out.println("Playing sound: " + soundUrl);
            Media sound = new Media(soundUrl.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading sound file: " + soundFileName);
        }
    }

}