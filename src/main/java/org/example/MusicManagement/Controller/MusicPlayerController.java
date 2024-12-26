package org.example.MusicManagement.Controller;
import org.example.MusicManagement.CustomInterface.CustomPlaybackListener;
import org.example.MusicManagement.Models.Music;
import org.example.MusicManagement.Models.MusicPlayer;

import java.util.ArrayList;

public class MusicPlayerController implements CustomPlaybackListener {
    public ArrayList<Music> arrMusic;
    private Music musicPlayedNow;
    MusicPlayer musicPlayer;
    private final String PATHBASESONG = "src/main/java/org/example/MusicManagement/publics/music/";
    MusicPlayerController(){
        arrMusic = new ArrayList<>();
        this.musicPlayer = new MusicPlayer(this);
        musicPlayedNow = null;
    }

    @Override
    public void onPlaybackFinished() {
        // Call playAgainMusic() when playback is finished
        if (musicPlayedNow != null) {
            playAgainMusic(); // Pass the appropriate music list
        }
    }

    public void playAgainMusic() {
        System.out.println("Hello");
    }

    public void startPlayMusic(Music targetMusicPlay){
        String source = PATHBASESONG + targetMusicPlay.getPathSong()+".mp3";
        if (getMusicPlayedNow() != null)
            stopAudio();
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
}
