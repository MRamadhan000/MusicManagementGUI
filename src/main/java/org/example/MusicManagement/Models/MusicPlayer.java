package org.example.MusicManagement.Models;

import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.PlaybackListener;
import org.example.MusicManagement.CustomInterface.CustomPlaybackListener;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;

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
    private int remainingTime; // Waktu yang tersisa untuk timer

    private CustomPlaybackListener playbackListener; // Add this line

    // Add a constructor or a setter to set the PlaybackListener
    public MusicPlayer(CustomPlaybackListener listener) {
        this.playbackListener = listener;
    }
    public void playAudio(String filePath) {
        System.out.println("path = |" + filePath+ "|");
        // Hentikan audio yang sedang diputar sebelum memulai audio baru
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
                    player = new Player(fileInputStream);  // Membuat objek player
                    System.out.println("Player created successfully: " + (player != null));

                    // Memulai pemutaran audio
                    isPlaying = true;
                    System.out.println("isPlaying set to true: " + isPlaying);

                    startTimer(durationSecond);
                    player.play();  // Memulai pemutaran audio

                } catch (Exception e) {
                    System.out.println("Error playing audio: " + e.getMessage());
                } finally {
                    isPlaying = false;
                    stopTimer();
                    System.out.println("END");

                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close(); // Menutup file setelah selesai
                            System.out.println("FileInputStream closed.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error closing FileInputStream: " + e.getMessage());
                    }

                    if (remainingTime == 0) {
                        playbackListener.onPlaybackFinished(); // Notify listener
                    }
                    playbackThread = null; // Set thread ke null untuk menghindari kebocoran
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
            // Pastikan player tidak null sebelum memanggil metode close
            if (player != null) {
                player.close(); // Hentikan pemutar
                System.out.println("Player closed successfully.");
                player = null; // Set player ke null untuk menghindari penggunaan kembali
            }

            // Pastikan file input stream juga ditutup
            if (fileInputStream != null) {
                fileInputStream.close();
                System.out.println("FileInputStream closed successfully.");
                fileInputStream = null; // Set fileInputStream ke null
            }

            // Set status playback
            isPlaying = false;
            isPaused = false;

            // Hentikan thread playback jika masih berjalan
            if (playbackThread != null && playbackThread.isAlive()) {
                playbackThread.interrupt(); // Interupsi thread playback
                playbackThread = null; // Set thread ke null untuk menghindari kebocoran
                System.out.println("Playback thread stopped successfully.");
            }

            // Hentikan timer jika sedang berjalan
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
        remainingTime = durationSecond; // Atur waktu awal sesuai durasi lagu

        timer = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (remainingTime > 0) {
                    System.out.println("Time Remaining: " + remainingTime + " seconds");
                    remainingTime--; // Kurangi waktu sisa
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
