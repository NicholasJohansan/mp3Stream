package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.net.URI;

public class PartialArtist {
  private int id;
  private String name;
  private URI avatarUrl;

  public PartialArtist(int id, String name, String avatarUrl) {
    this.id = id;
    this.name = name;
    this.avatarUrl = URI.create(avatarUrl);
  }

  public String getName() {
    return name;
  }
}
