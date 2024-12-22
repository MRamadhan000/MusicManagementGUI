package org.example.MusicManagement.Controller;

import org.example.MusicManagement.DB;
import org.example.MusicManagement.model.Music;
import org.example.MusicManagement.model.MusicPlayer;
import org.example.MusicManagement.view.Body;
import org.example.MusicManagement.view.Footer;
import org.example.MusicManagement.view.Header;
import org.example.MusicManagement.view.MainFrame;

import java.util.ArrayList;
public class MusicController {
    private ArrayList<Music> arrMusic;
    private MainFrame mainFrame;
    private Body body; // Referensi ke Body
    private MusicPlayer musicPlayer;
    private String currentMusic = "";
    private String nextMusic = "";

    public MusicController() {
        // Inisialisasi view utama
        mainFrame = new MainFrame();
        arrMusic = DB.getDataDB();
        musicPlayer = new MusicPlayer();
        // Load data atau setup awal
        setupView();
    }
    private void setupView() {
        // Panggil bagian view seperti Header, Body, Footer
        mainFrame.setHeader(new Header(this));
        setUpBody();
        mainFrame.setFooter(new Footer());
    }

    //targeSongPlay = path
    public void startPlayMusic(String targetSongPath,String songName){
        String source = "src/main/java/org/example/MusicManagement/assets/music/" + targetSongPath + ".mp3";
        this.nextMusic = songName;
        if (currentMusic != null) {
            currentMusic = songName;
            musicPlayer.stopAudio();
            musicPlayer.playAudio(source);
            mainFrame.setFooter(new Footer(musicPlayer,songName));
            this.nextMusic = null;
        }else{
            musicPlayer.playAudio(nextMusic);
            this.currentMusic = targetSongPath;
            mainFrame.setFooter(new Footer(musicPlayer,songName));
            this.nextMusic = null;
        }
    }
    public void showApp() {
        mainFrame.setVisible(true);
    }

    public void addMusic(Music music) {
        arrMusic.add(music); // Menambahkan data ke list
        System.out.println("Music added: " + music.getSongName() + " in arrayList");
        DB.addDataToDB(music);
        setUpBody();
    }

    private void setUpBody() {
        body = new Body(this,arrMusic);
        mainFrame.setBody(body);
    }

    public boolean updateMusic(String targetSongName, String newSongName, String newArtistName, String newAlbum, String newPathSong) {
        for (Music music : arrMusic) {
            if (music.getSongName().equalsIgnoreCase(targetSongName)) {
                String oldNameSong = music.getSongName();
                String oldArtistName = music.getArtistName();
                String oldAlbum = music.getAlbum();
                String oldPathSong = music.getPathSong();
                music.setSongName(newSongName);
                music.setArtistName(newArtistName);
                music.setAlbum(newAlbum);
                music.setPathSong(newPathSong);
                System.out.println("Music updated: " + music);

                // Call DB.editToDB with the new song name
                boolean dbUpdated = DB.editToDB(targetSongName,newSongName,newArtistName,newAlbum,newPathSong);
                if (dbUpdated){
                    setUpBody();
                }

                if (currentMusic.equalsIgnoreCase(targetSongName)){
                    if (oldPathSong.equalsIgnoreCase(newPathSong)){
                        if (!oldNameSong.equalsIgnoreCase(newSongName) || !oldArtistName.equalsIgnoreCase(newArtistName) || !oldAlbum.equalsIgnoreCase(newAlbum))
                            mainFrame.setFooter(new Footer(musicPlayer, newSongName));
                    }else{
                        mainFrame.setFooter(new Footer());
                        musicPlayer.pauseAudio();
                    }
                    currentMusic = newSongName;
                }
                return dbUpdated;
            }
        }
        System.out.println("Music not found with name: " + targetSongName);
        return false;
    }

    public void setUpFooter(String songName){
        if (songName.equalsIgnoreCase(currentMusic)) {
            musicPlayer.pauseAudio();
            mainFrame.setFooter(new Footer());
        }
    }

    public boolean deleteMusic(String targetSongName) {
        // Step 1: Check if the song exists in the array
        for (Music music : arrMusic) {
            if (music.getSongName().equalsIgnoreCase(targetSongName)) {
                // Step 2: Remove the song from the array
                arrMusic.remove(music);

                // Step 3: Call the deleteToDB method to delete the song from the database
                if (DB.deleteToDB(targetSongName)) {
                    System.out.println("Song successfully removed from database.");
                    setUpBody();
                    if(currentMusic.equalsIgnoreCase(targetSongName)) {
                        musicPlayer.pauseAudio();
                        mainFrame.setFooter(new Footer());
                    }
                    return true; // Return true if deletion from both array and DB was successful
                } else {
                    System.out.println("Failed to delete the song from database.");
                    return false; // Return false if deletion from DB failed
                }
            }
        }
        System.out.println("Song not found in array.");
        return false; // Return false if song is not found in the array
    }
    private void showDataList(){
        for (Music music : arrMusic){
            System.out.println("Songname : " + music.getSongName() + " Artist Name : " + music.getArtistName() + " Album : " + music.getAlbum() + " Path Song : " + music.getPathSong() );
        }
    }
}
