package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.net.URI;

public class Playlist {
    private String coverUrl;
    private String title;
    private int duration;
    private int songCount;
    private int id;
    private PartialArtist artist;

    public Playlist(String coverUrl, String title, int duration, int songCount, int id, PartialArtist artist) {
        this.coverUrl = coverUrl;
        this.title = title;
        this.duration = duration;
        this.songCount = songCount;
        this.id = id;
        this.artist = artist;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getFriendlyDuration() {
        int duration = this.duration / 1000;
        int seconds = duration % 60;
        int minutes = duration / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);

    }

    public int getSongCount() {
        return songCount;
    }

    public int getId() {
        return id;
    }

    public PartialArtist getArtist() {
        return artist;
    }
}
