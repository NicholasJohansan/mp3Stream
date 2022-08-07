package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
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
  public String getBannerUrl() {
    return bannerUrl;
  }
  public int getId() {
    return id;
  }

  // Parcelable implementation
  protected Artist(Parcel in) {
    id = in.readInt();
    name = in.readString();
    bannerUrl = in.readString();
    avatarUrl = in.readString();
    songCount = in.readInt();
    playlistCount = in.readInt();
  }

  public static final Creator<Artist> CREATOR = new Creator<Artist>() {
    @Override
    public Artist createFromParcel(Parcel in) {
      return new Artist(in);
    }

    @Override
    public Artist[] newArray(int size) {
      return new Artist[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(id);
    parcel.writeString(name);
    parcel.writeString(bannerUrl);
    parcel.writeString(avatarUrl);
    parcel.writeInt(songCount);
    parcel.writeInt(playlistCount);
  }
}
