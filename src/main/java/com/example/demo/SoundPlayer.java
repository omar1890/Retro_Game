package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    private static final String GAMEOVER_SOUND_NAME = "/com/example/demo/sounds/GAME_OVER.mp3";
    private static final String WINNER_SOUND_NAME = "/com/example/demo/sounds/winner.mp3";

    public static void playGameOverSound() {
        try {
            Media sound = new Media(SoundPlayer.class.getResource(GAMEOVER_SOUND_NAME).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void playWinnerSound() {
        try {
            Media sound = new Media(SoundPlayer.class.getResource(WINNER_SOUND_NAME).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }
}
