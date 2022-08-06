package co.carrd.njportfolio.todolist.Models;

import java.util.List;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LibraryPlaylist extends RealmObject {
    @PrimaryKey private UUID id;
    @Required private String name;
    @Required private List<Integer> trackIds;

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

    public List<Integer> getTrackIds() {
        return trackIds;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }
}
