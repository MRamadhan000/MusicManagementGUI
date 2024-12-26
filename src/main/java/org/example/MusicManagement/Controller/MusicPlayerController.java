package org.example.MusicManagement.Controller;
import org.example.MusicManagement.Models.Music;
import org.example.MusicManagement.Models.MusicPlayer;

import java.util.ArrayList;

public class MusicPlayerController {
    private Music musicPlayedNow;
    MusicPlayer musicPlayer;
    private final String PATHBASESONG = "src/main/java/org/example/MusicManagement/publics/music/";
    MusicPlayerController(MusicPlayer musicPlayer){
        this.musicPlayer = musicPlayer;
        musicPlayedNow = null;
    }

    public void playAgainMusic(ArrayList<Music> music){

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

}
