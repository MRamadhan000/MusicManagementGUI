package org.example.MusicManagement.Models;

import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.player.Player;
import javax.swing.*;
import java.io.FileInputStream;

// library for mp3 length https://github.com/mpatric/mp3agic.git
//

public class MusicPlayer {
    private Player player;
    private Thread playbackThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private String currentFilePath;
    private long pauseLocation = 0;
    private long totalSongLength = 0;
    private Timer timer;
    private boolean isPlayAgin = false;
    private FileInputStream fileInputStream;
    public void playAudio(String filePath) {
        try {
            fileInputStream = new FileInputStream(filePath);
            currentFilePath = filePath;
            totalSongLength = fileInputStream.available();

            Mp3File mp3file = new Mp3File(filePath);
            System.out.println("Length musik : " + mp3file.getLengthInSeconds());
            int durationSecond = ((int) mp3file.getLengthInSeconds()) + 2;

            playbackThread = new Thread(() -> {
                try {
                    player = new Player(fileInputStream);
                    isPlaying = true;
                    startTimer(durationSecond);

                    player.play();
                } catch (Exception e) {
                    System.out.println("Error playing audio: " + e.getMessage());
                } finally {
                    isPlaying = false;
                    stopTimer();
                    System.out.println("END");
                }
            });

            playbackThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Music not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error initializing audio: " + e.getMessage());
        }
        System.out.println("THIS ISHDE");
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
    private void startTimer(int durationSecond) {
        System.out.println("Starting timer...");
        timer = new Timer(1000, new AbstractAction() {
            int timeRemaining = durationSecond;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (timeRemaining > 0) {
                    System.out.println("Time Remaining: " + timeRemaining + " seconds");
                    timeRemaining--;
                } else {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Timer Finished: Song duration complete!");
                    isPlayAgin = true;
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            System.out.println("Timer stopped.");
        }
    }

    private void resetTimer(int newDurationSecond) {
        // Stop the current timer if it's running
        stopTimer();

        // Reset the timeRemaining to the new duration
        timer = new Timer(1000, new AbstractAction() {
            int timeRemaining = newDurationSecond; // Use the new duration

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (timeRemaining > 0) {
                    System.out.println("Time Remaining: " + timeRemaining + " seconds");
                    timeRemaining--;
                } else {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Timer Finished: Song duration complete!");
                    isPlayAgin = true;
                }
            }
        });

        timer.setInitialDelay(0);
        timer.start();
        System.out.println("Timer reset to " + newDurationSecond + " seconds.");
    }

    public boolean isPlayAgin() {
        return isPlayAgin;
    }
}
