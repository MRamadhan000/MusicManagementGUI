package org.example.MusicManagement.Models;

public class Music {
    private String songName;
    private String artistName;
    private String album;
    private String pathSong;
    public Music(String songName, String artistName,String album ,String pathSong) {
        this.songName = songName;
        this.artistName = artistName;
        this.album = album;
        this.pathSong = pathSong;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
