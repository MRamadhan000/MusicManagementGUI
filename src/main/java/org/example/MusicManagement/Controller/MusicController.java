package org.example.MusicManagement.Controller;

import org.example.MusicManagement.Database;
import org.example.MusicManagement.Models.Music;
import org.example.MusicManagement.Views.Body;
import org.example.MusicManagement.Views.Footer;
import org.example.MusicManagement.Views.Header;
import org.example.MusicManagement.Views.MainFrame;

import java.util.ArrayList;
public class MusicController{
    private ArrayList<Music> arrMusic;
    private MainFrame mainFrame;
    private Database db;
    private MusicPlayerController musicPlayerController;

    public MusicController() {
        // initilize db
        db = new Database();

        // initialize main view
        mainFrame = new MainFrame();
        arrMusic = db.getDataDB();

        // Set up for music player
        musicPlayerController = new MusicPlayerController(this);

        // Load view
        setupView();
    }
    private void setupView() {
        // call the view section: Header, Body and Footer
        mainFrame.setHeader(new Header(this));
        mainFrame.setBody(new Body(this));
        mainFrame.setFooter(new Footer());
    }
    public void showApp() {
        mainFrame.setVisible(true);
    }

    public void startPlayMusic(Music targetMusicPlay){
        showDataList();
        musicPlayerController.setArrMusic(arrMusic);
        musicPlayerController.startPlayMusic(targetMusicPlay);
        mainFrame.setFooter(new Footer(this,musicPlayerController.getMusicPlayedNow().getSongName()));
    }

    public void addMusic(Music music) {
        // add data to arraylist
        arrMusic.add(music);
        System.out.println("Music added: " + music.getSongName() + " in arrayList");

        // add data to database
        db.addDataToDB(music);
//        setUpBody();
        mainFrame.setBody(new Body(this));
    }
    public boolean updateMusic(String targetSongName, String newSongName, String newArtistName, String newAlbum, String newPathSong) {
        for (Music targetMusic : arrMusic) {
            if (targetMusic.getSongName().equalsIgnoreCase(targetSongName)) {
                String oldNameSong = targetMusic.getSongName();
                String oldArtistName = targetMusic.getArtistName();
                String oldAlbum = targetMusic.getAlbum();
                String oldPathSong = targetMusic.getPathSong();
                targetMusic.setSongName(newSongName);
                targetMusic.setArtistName(newArtistName);
                targetMusic.setAlbum(newAlbum);
                targetMusic.setPathSong(newPathSong);
                System.out.println("Music updated: " + targetMusic);

                // Edit data to db
                boolean dbUpdated = db.editToDB(targetSongName,newSongName,newArtistName,newAlbum,newPathSong);

                // Reload body
                if (dbUpdated)
                    mainFrame.setBody(new Body(this));

                // Check if the music being played is the same as the target and reload the footer.
                if (musicPlayerController.getMusicPlayedNow() != null){
                    if (musicPlayerController.getMusicPlayedNow().getSongName().equalsIgnoreCase(targetSongName)){
                        if (oldPathSong.equalsIgnoreCase(newPathSong)){
                            if (!oldNameSong.equalsIgnoreCase(newSongName) || !oldArtistName.equalsIgnoreCase(newArtistName) || !oldAlbum.equalsIgnoreCase(newAlbum))
                                mainFrame.setFooter(new Footer(this, newSongName));
                        }else{
                            mainFrame.setFooter(new Footer());
                            this.stopAudio();
                        }
                        musicPlayerController.setMusicPlayedNow(targetMusic);
                    }
                }
                return dbUpdated;
            }
        }
        System.out.println("Music not found with name: " + targetSongName);
        return false;
    }

    public boolean deleteMusic(String targetSongName) {
        for (Music music : arrMusic) {
            if (music.getSongName().equalsIgnoreCase(targetSongName)) {
                arrMusic.remove(music);

                // Delete data to db
                if (db.deleteToDB(targetSongName)) {
                    System.out.println("Song successfully removed from database.");

                    // Reload body
                    mainFrame.setBody(new Body(this));

                    // Reload the regular footer if the song being played is deleted
                    if(musicPlayerController.getMusicPlayedNow().getSongName().equalsIgnoreCase(targetSongName)) {
                        this.stopAudio();
                        musicPlayerController.setMusicPlayedNow(null);
                        mainFrame.setFooter(new Footer());
                    }
                    return true;
                } else {
                    System.out.println("Failed to delete the song from database.");
                    return false;
                }
            }
        }
        System.out.println("Song not found in array.");
        return false;
    }

    public void stopAudio(){
        musicPlayerController.stopAudio();
    }
    public void pauseAudio(){
        musicPlayerController.pauseAudio();
    }
    public void resumeAudio(){
        musicPlayerController.resumeAudio();
    }

    private void showDataList(){
        for (Music music : arrMusic){
            System.out.println("Songname : " + music.getSongName() + " Artist Name : " + music.getArtistName() + " Album : " + music.getAlbum() + " Path Song : " + music.getPathSong() );
        }
    }
    public ArrayList<Music> getArrMusic() {
        return arrMusic;
    }
}
