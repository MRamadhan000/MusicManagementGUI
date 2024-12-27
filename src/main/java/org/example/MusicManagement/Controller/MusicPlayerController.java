package org.example.MusicManagement.Controller;
import org.example.MusicManagement.CustomInterface.CustomPlaybackListener;
import org.example.MusicManagement.Models.Music;
import org.example.MusicManagement.Models.MusicPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MusicPlayerController implements CustomPlaybackListener {
    public ArrayList<Music> arrMusic;
    private Music musicPlayedNow;
    private MusicPlayer musicPlayer;
    private final String PATHBASESONG = "src/main/java/org/example/MusicManagement/publics/music/";
    private MusicController musicController;
    MusicPlayerController(MusicController musicController){
        this.musicController = musicController;
        this.musicPlayer = new MusicPlayer(this);
        musicPlayedNow = null;
        arrMusic = new ArrayList<>();
    }

    // method for trigger music play again when previous song completed
    @Override
    public void onPlaybackFinished() {
        this.musicPlayer = new MusicPlayer(this);
        playAgainMusic();
    }

    public void playAgainMusic() {
        showDataList(arrMusic);
        ArrayList<Music> validSongs = new ArrayList<>();
        for (Music music : arrMusic) {
            String filePath = PATHBASESONG + music.getPathSong() + ".mp3";
            File musicFile = new File(filePath);
            if (musicFile.exists()) {
                validSongs.add(music);
            }
        }
        System.out.println("=======");

        showDataList(arrMusic);

        // If there are no valid songs, return early
        if (validSongs.isEmpty()) {
            System.out.println("No valid songs found to play.");
            return;
        }

        Random random = new Random();
        Music randomMusic = null;

        // Ensure the song chosen is different from the currently playing song
        do {
            randomMusic = validSongs.get(random.nextInt(validSongs.size()));
        } while (randomMusic.equals(musicPlayedNow));

        System.out.println("This is the song that will be played: |" + randomMusic.getPathSong() + "|");

        musicPlayedNow = randomMusic;

        musicController.startPlayMusic(randomMusic);
        System.out.println("Playing: " + randomMusic.getSongName());
    }

    public void startPlayMusic(Music targetMusicPlay){
        String source = PATHBASESONG + targetMusicPlay.getPathSong()+".mp3";
        setMusicPlayedNow(targetMusicPlay);
        musicPlayer.playAudio(source);
    }

    public void stopAudio(){
        musicPlayer.stopAudio();
    }

    public void pauseAudio(){
        musicPlayer.pauseAudio();
    }

    public void resumeAudio(){
        musicPlayer.resumeAudio();
    }

    public Music getMusicPlayedNow() {
        return musicPlayedNow;
    }

    public void setMusicPlayedNow(Music musicPlayedNow) {
        this.musicPlayedNow = musicPlayedNow;
    }

    public void setArrMusic(ArrayList<Music> arrMusic) {
        this.arrMusic = arrMusic;
    }
    private void showDataList(ArrayList<Music> arrMusic){
        for (Music music : arrMusic){
            System.out.println("Songname : " + music.getSongName() + " Artist Name : " + music.getArtistName() + " Album : " + music.getAlbum() + " Path Song : " + music.getPathSong() );
        }
    }
}

