package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.util.List;

public class PlaylistCollection {
  private List<Playlist> playlists;
  private int totalResults;
  private String nextUrl;

  public PlaylistCollection(List<Playlist> playlists, int totalResults, String nextUrl) {
    this.playlists = playlists;
    this.totalResults = totalResults;
    this.nextUrl = nextUrl;
  }

  public PlaylistCollection() {}

  public List<Playlist> getPlaylists() {
    return playlists;
  }
  public String getNextUrl() { return nextUrl; }

  
}
