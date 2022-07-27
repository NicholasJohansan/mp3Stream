package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.util.List;

public class SongCollection {
  private List<Song> songs;
  private int totalResults;
  private String nextUrl;

  public SongCollection(List<Song> songs, int totalResults, String nextUrl) {
    this.songs = songs;
    this.totalResults = totalResults;
    this.nextUrl = nextUrl;
  }

  public SongCollection() {}

  public List<Song> getSongs() {
    return songs;
  }
  public String getNextUrl() { return nextUrl; }

  
}
