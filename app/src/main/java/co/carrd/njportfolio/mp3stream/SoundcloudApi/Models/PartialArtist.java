package co.carrd.njportfolio.mp3stream.SoundcloudApi.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class PartialArtist implements Parcelable {
  private int id;
  private String name;
  private String avatarUrl;

  public PartialArtist(int id, String name, String avatarUrl) {
    this.id = id;
    this.name = name;
    this.avatarUrl = avatarUrl;
  }

  public String getName() {
    return name;
  }

  // Parcelable implementation
  protected PartialArtist(Parcel in) {
    id = in.readInt();
    name = in.readString();
    avatarUrl = in.readString();
  }

  public static final Creator<PartialArtist> CREATOR = new Creator<PartialArtist>() {
    @Override
    public PartialArtist createFromParcel(Parcel in) {
      return new PartialArtist(in);
    }

    @Override
    public PartialArtist[] newArray(int size) {
      return new PartialArtist[size];
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
    parcel.writeString(avatarUrl);
  }
}
