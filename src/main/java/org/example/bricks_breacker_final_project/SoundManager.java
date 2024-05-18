package org.example.bricks_breacker_final_project;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    // In SoundManager
    public static void playSound(String soundFileName) {
        // Use getResource with a leading slash for an absolute path within the classpath
        URL soundUrl = SoundManager.class.getResource("/" + soundFileName);
        Media sound = new Media(soundUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
