package org.example.MusicManagement.Models;

import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.PlaybackListener;
import org.example.MusicManagement.CustomInterface.CustomPlaybackListener;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;

// library for mp3 length https://github.com/mpatric/mp3agic.git
// libarary for mp3 player https://github.com/umjammer/jlayer.git

public class MusicPlayer {
    private Player player;
    private Thread playbackThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private String currentFilePath;
    private long pauseLocation = 0;
    private long totalSongLength = 0;
    private Timer timer;
    private FileInputStream fileInputStream;
    private int remainingTime; //timeRemaining for music duration
    private CustomPlaybackListener playbackListener; // use CostuumPlayBackListener

    public MusicPlayer(CustomPlaybackListener listener) {
        this.playbackListener = listener;
    }
    public void playAudio(String filePath) {
        stopAudio();
        try {
            fileInputStream = new FileInputStream(filePath);
            currentFilePath = filePath;
            totalSongLength = fileInputStream.available();

            // Mencetak hasilnya
            System.out.println("File Path: " + currentFilePath);
            System.out.println("Total song length (in bytes): " + totalSongLength);

            Mp3File mp3file = new Mp3File(filePath);
            System.out.println("Length musik: " + mp3file.getLengthInSeconds());
            int durationSecond = (int) mp3file.getLengthInSeconds();

            playbackThread = new Thread(() -> {
                try {
                    player = new Player(fileInputStream);  // Make object player
                    System.out.println("Player created successfully: " + (player != null));

                    // Memulai pemutaran audio
                    isPlaying = true;
                    System.out.println("isPlaying set to true: " + isPlaying);

                    startTimer(durationSecond);
                    player.play();  // startPlayAudio

                } catch (Exception e) {
                    System.out.println("Error playing audio: " + e.getMessage());
                } finally {
                    isPlaying = false;
                    stopTimer();
                    System.out.println("END");

                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close(); // Close file
                            System.out.println("FileInputStream closed.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error closing FileInputStream: " + e.getMessage());
                    }

                    if (remainingTime == 0)
                        playbackListener.onPlaybackFinished(); // Notify listener

                    playbackThread = null; // Set thread ke null
                }
            });
            playbackThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Music not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error initializing audio: " + e.getMessage());
        }
    }

    public void stopAudio() {
        try {
            // Make sure player was close
            if (player != null) {
                player.close();
                System.out.println("Player closed successfully.");
                player = null;
            }

            // Make sure fileInputStream was close
            if (fileInputStream != null) {
                fileInputStream.close();
                System.out.println("FileInputStream closed successfully.");
                fileInputStream = null; // Set fileInputStream ke null
            }

            // Set status playback
            isPlaying = false;
            isPaused = false;

            // Stop thread playback if it is still running
            if (playbackThread != null && playbackThread.isAlive()) {
                playbackThread.interrupt(); // Playback thread interrupt
                playbackThread = null; // Set thread to null to avoid leaks
                System.out.println("Playback thread stopped successfully.");
            }

            // Pause timer
            if (timer != null && timer.isRunning()) {
                timer.stop();
                System.out.println("Timer stopped successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error while stopping audio: " + e.getMessage());
        }
    }


    private void startTimer(int durationSecond) {
        System.out.println("Starting timer...");
        remainingTime = durationSecond; // Set the start time according to the duration of the song

        timer = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (remainingTime > 0) {
                    System.out.println("Time Remaining: " + remainingTime + " seconds");
                    remainingTime--;
                } else {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Timer Finished: Song duration complete!");
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

    public void pauseAudio() {
        if (isPlaying && !isPaused) {
            try {
                pauseLocation = totalSongLength - fileInputStream.available();
                player.close(); // Stop the current player
                isPaused = true;
                isPlaying = false;

                // Pause timer and store remaining time
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                    System.out.println("Timer paused with " + remainingTime + " seconds remaining.");
                }
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

                // Resume timer from remaining time
                startTimer(remainingTime);
            } catch (Exception e) {
                System.out.println("Error resuming audio: " + e.getMessage());
            }
        }
    }
}
