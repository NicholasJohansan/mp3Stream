package co.carrd.njportfolio.mp3stream.SoundcloudApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.PartialArtist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.PlaylistCollection;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.SongCollection;

public class ApiParser {

    public static PlaylistCollection parsePlaylistCollection(String playlistCollectionStringData) {
        try {
            JSONObject obj = new JSONObject(playlistCollectionStringData);
            JSONArray playlistsDataArray = obj.getJSONArray("collection");
            List<Playlist> playlistsArray = new ArrayList<>();
            for (int i = 0; i < playlistsDataArray.length(); i++) {
                JSONObject playlistData = (JSONObject) playlistsDataArray.get(i);
                playlistsArray.add(ApiParser.parsePlaylist(playlistData));
            }

            return new PlaylistCollection(
                    playlistsArray,
                    obj.getInt("total_results"),
                    obj.has("next_href")
                            ? obj.getString("next_href")
                            : null
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Playlist parsePlaylist(JSONObject playlistData) {
        try {
            String title = playlistData.getString("title");
            int duration = playlistData.getInt("duration");
            int id = playlistData.getInt("id");
            String coverUrl = playlistData.getString("artwork_url");
            int songCount = playlistData.getInt("track_count");

            // If playlist has no cover art
            if (coverUrl.equals("null")) {
                // Use cover art of first track
                JSONArray playlistSongsDataArray = playlistData.getJSONArray("tracks");
                if (playlistSongsDataArray.length() > 0) {
                    JSONObject firstSong = (JSONObject) playlistSongsDataArray.get(0);
                    coverUrl = firstSong.getString("artwork_url");
                }
            }

            // Get alternate resolution image
            coverUrl = coverUrl.replace("large", "t500x500");
            PartialArtist partialArtist = parsePartialArtist(playlistData.getJSONObject("user"));

            return new Playlist(coverUrl, title, duration, songCount, id, partialArtist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SongCollection parseSongCollection(String songCollectionDataString) {
        try {
            JSONObject obj = new JSONObject(songCollectionDataString);
            JSONArray songsDataArray = obj.getJSONArray("collection");
            List<Song> songsArray = new ArrayList<>();
            for (int i = 0; i < songsDataArray.length(); i++) {
                JSONObject songData = (JSONObject) songsDataArray.get(i);
                songsArray.add(ApiParser.parseSong(songData));
            }

            return new SongCollection(
                    songsArray,
                    obj.getInt("total_results"),
                    obj.has("next_href")
                            ? obj.getString("next_href")
                            : null
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Song parseSong(JSONObject songData) {
        try {
            String title = songData.getString("title");
            int duration = songData.getInt("duration");
            int id = songData.getInt("id");
            String coverUrl = songData.getString("artwork_url").replace("large", "t500x500");

            String partialStreamUrl = songData.getJSONObject("media")
                    .getJSONArray("transcodings")
                    .getJSONObject(0)
                    .getString("url");

            PartialArtist partialArtist = parsePartialArtist(songData.getJSONObject("user"));

            return new Song(coverUrl, partialStreamUrl, title, duration, id, partialArtist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PartialArtist parsePartialArtist(JSONObject artistData) {
        try {
            String artistName = artistData.getString("username");
            int artistId = artistData.getInt("id");
            String artistAvatarUrl = artistData.getString("avatar_url");
            return new PartialArtist(artistId, artistName, artistAvatarUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> parseSearchSuggestions(String searchSuggestionsStringData) {
        try {
            List<String> searchSuggestions = new ArrayList<>();
            JSONObject obj = new JSONObject(searchSuggestionsStringData);
            JSONArray searchSuggestionsArray = obj.getJSONArray("collection");
            for (int i = 0; i < searchSuggestionsArray.length(); i++) {
                searchSuggestions.add((String) ((JSONObject) searchSuggestionsArray.get(i)).get("query"));
            }
            return searchSuggestions;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
