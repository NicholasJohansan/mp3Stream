package co.carrd.njportfolio.mp3stream.Library.Models;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LibraryPlaylist extends RealmObject {
    @PrimaryKey private UUID id;
    @Required private String name;
    @Required private RealmList<Integer> trackIds;

    public LibraryPlaylist(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public LibraryPlaylist(LibraryPlaylist libraryPlaylist) {
        this.name = libraryPlaylist.getName();
        this.id = libraryPlaylist.getId();
        this.trackIds = libraryPlaylist.getTrackIds();
    }

    public LibraryPlaylist() {}

    public UUID getId() {
        return id;
    }

    public RealmList<Integer> getTrackIds() {
        return trackIds;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }
}
