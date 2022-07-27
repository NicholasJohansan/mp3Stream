package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.net.URI;

public class Song {
  private String coverUrl;
  private URI partialStreamUrl;
  private String title;
  private int duration;
  private int id;
  private PartialArtist artist;

  public Song(String coverUrl, String partialStreamUrl, String title, int duration, int id, PartialArtist artist) {
    this.coverUrl = coverUrl;
    this.partialStreamUrl = URI.create(partialStreamUrl);
    this.title = title;
    this.duration = duration;
    this.id = id;
    this.artist = artist;
  }

  public String getCoverUrl() {
    return this.coverUrl;
  }

  public URI getPartialStreamUrl() {
    return this.partialStreamUrl;
  }
  
  public String getTitle() {
    return this.title;
  }

  public int getDuration() {
    return this.duration;
  }

  public int getId() {
    return this.id;
  }

  public PartialArtist getArtist() {
    return this.artist;
  }
  
}
