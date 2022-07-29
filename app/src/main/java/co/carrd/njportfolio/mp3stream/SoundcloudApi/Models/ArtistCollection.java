package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.util.List;

public class ArtistCollection {
  private List<Artist> artists;
  private int totalResults;
  private String nextUrl;

  public ArtistCollection(List<Artist> artists, int totalResults, String nextUrl) {
    this.artists = artists;
    this.totalResults = totalResults;
    this.nextUrl = nextUrl;
  }

  public List<Artist> getArtists() {
    return artists;
  }
  public String getNextUrl() { return nextUrl; }

  
}
