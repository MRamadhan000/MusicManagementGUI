package org.example.MusicManagement.model;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class MusicPlayer {
    private Player player;
    private Thread playbackThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    private String currentFilePath;
    private long pauseLocation = 0;
    private long totalSongLength = 0;
    private FileInputStream fileInputStream;
    public void playAudio(String filePath) {
        if (isPlaying) {
            System.out.println("Audio is already playing!");
            return;
        }

        try {
            fileInputStream = new FileInputStream(filePath);
            currentFilePath = filePath;
            totalSongLength = fileInputStream.available();

            playbackThread = new Thread(() -> {
                try {
                    player = new Player(fileInputStream);
                    isPlaying = true;
                    player.play();
                } catch (Exception e) {
                    System.out.println("Error playing audio: " + e.getMessage());
                } finally {
                    isPlaying = false;
                }
            });

            playbackThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Music not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error initializing audio: " + e.getMessage());
        }
        System.out.println("HELLO");
    }

    public void stopAudio() {
        if (player != null) {
            player.close(); // Stop the player
            isPlaying = false;
            isPaused = false;
        }

        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt(); // Stop the thread if still running
        }
    }

    public void pauseAudio() {
        if (isPlaying && !isPaused) {
            try {
                pauseLocation = totalSongLength - fileInputStream.available();
                player.close(); // Stop the current player
                isPaused = true;
                isPlaying = false;
            } catch (Exception e) {
                System.out.println("Error pausing audio: " + e.getMessage());
            }
        }
    }

    public void resumeAudio() {
        if (isPaused) {
            try {
                fileInputStream = new FileInputStream(currentFilePath);
                fileInputStream.skip(pauseLocation); // Skip to the last pause location

                playbackThread = new Thread(() -> {
                    try {
                        player = new Player(fileInputStream);
                        isPlaying = true;
                        isPaused = false;
                        player.play();
                    } catch (Exception e) {
                        System.out.println("Error resuming audio: " + e.getMessage());
                    } finally {
                        isPlaying = false;
                    }
                });

                playbackThread.start();
            } catch (Exception e) {
                System.out.println("Error resuming audio: " + e.getMessage());
            }
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
