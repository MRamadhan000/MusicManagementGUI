package org.example.MusicManagement.Controller;

import org.example.MusicManagement.Database;
import org.example.MusicManagement.Models.Music;
import org.example.MusicManagement.Models.MusicPlayer;
import org.example.MusicManagement.Views.Body;
import org.example.MusicManagement.Views.Footer;
import org.example.MusicManagement.Views.Header;
import org.example.MusicManagement.Views.MainFrame;

import java.util.ArrayList;
public class MusicController {
    private ArrayList<Music> arrMusic;
    private MainFrame mainFrame;
    private Body body;
    private MusicPlayer musicPlayer;
    private String currentMusic = "";
    private String nextMusic = "";
    private Database db;
    private final String BASEPATHSONG = "src/main/java/org/example/MusicManagement/publics/music/";
    private Music musicNow;

    public MusicController() {
        // initilize db
        db = new Database();
        // initialize main view
        mainFrame = new MainFrame();
        arrMusic = db.getDataDB();
        musicPlayer = new MusicPlayer();
        musicNow = null;

        // Load view
        setupView();
    }

    private void setupView() {
        // call the view section: Header, Body and Footer
        mainFrame.setHeader(new Header(this));
//        setUpBody();
        mainFrame.setBody(new Body(this));
        mainFrame.setFooter(new Footer());
    }
    public void showApp() {
        mainFrame.setVisible(true);
    }

    public void startPlayMusic(String targetSongPath,String newSongName){
        String source = BASEPATHSONG + targetSongPath + ".mp3";
        this.nextMusic = newSongName;

        // check if there is still anything playing
        if (currentMusic != null) {
            currentMusic = newSongName;
            this.stopAudio();
            this.startPlayAudio(source);
            mainFrame.setFooter(new Footer(this,newSongName));
            this.nextMusic = null;
        }else{
            this.startPlayAudio(source);
            this.currentMusic = targetSongPath;
            mainFrame.setFooter(new Footer(this,newSongName));
            this.nextMusic = null;
        }
    }

    public void startPlayAudio(String source){
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

                // Edit data to db
                boolean dbUpdated = db.editToDB(targetSongName,newSongName,newArtistName,newAlbum,newPathSong);

                // Reload body
                if (dbUpdated){
//                    setUpBody();
                    mainFrame.setBody(new Body(this));
                }

                // Check if the music being played is the same as the target and reload the footer.
                if (currentMusic.equalsIgnoreCase(targetSongName)){
                    if (oldPathSong.equalsIgnoreCase(newPathSong)){
                        if (!oldNameSong.equalsIgnoreCase(newSongName) || !oldArtistName.equalsIgnoreCase(newArtistName) || !oldAlbum.equalsIgnoreCase(newAlbum))
                            mainFrame.setFooter(new Footer(this, newSongName));
                    }else{
                        mainFrame.setFooter(new Footer());
                        this.stopAudio();
                    }
                    currentMusic = newSongName;
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

                    // Reload ulang body
//                    setUpBody();
                    mainFrame.setBody(new Body(this));

                    // Reload the regular footer if the song being played is deleted
                    if(currentMusic.equalsIgnoreCase(targetSongName)) {
                        this.stopAudio();
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
    private void showDataList(){
        for (Music music : arrMusic){
            System.out.println("Songname : " + music.getSongName() + " Artist Name : " + music.getArtistName() + " Album : " + music.getAlbum() + " Path Song : " + music.getPathSong() );
        }
    }
    public ArrayList<Music> getArrMusic() {
        return arrMusic;
    }
}
