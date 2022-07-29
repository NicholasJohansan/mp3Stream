package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.net.URI;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiUtils;

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
        return ApiUtils.getFriendlyDuration(duration);
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
