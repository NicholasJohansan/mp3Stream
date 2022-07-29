package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import java.net.URI;

public class Artist {
  private int id;
  private String name;
  private String bannerUrl;
  private String avatarUrl;
  private int songCount;
  private int playlistCount;

  public Artist(int id, String name, String bannerUrl, String avatarUrl, int songCount, int playlistCount) {
    this.id = id;
    this.name = name;
    this.bannerUrl = bannerUrl;
    this.avatarUrl = avatarUrl;
    this.songCount = songCount;
    this.playlistCount = playlistCount;
  }

  public String getName() {
    return name;
  }
  public String getAvatarUrl() {
    return avatarUrl;
  }
  public int getSongCount() {
    return songCount;
  }
  public int getPlaylistCount() {
    return playlistCount;
  }

}
