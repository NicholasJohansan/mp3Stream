package co.carrd.njportfolio.mp3stream.SoundcloudApi;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.PartialArtist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.PlaylistCollection;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.SongCollection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of a wrapper for Soundcloud API
 */
public class ApiWrapper {
  private static OkHttpClient client;
  private static String clientId;
  private static ApiUtils apiUtils;
  
  public ApiWrapper() {
    if (client == null) {
      client = new OkHttpClient();
      apiUtils = new ApiUtils(client);
      this.setClientId();
    }
  }

  public void searchPlaylists(String query, Consumer<PlaylistCollection> consumer) {
    // Construct endpoint url
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/playlists_without_albums?q=" + query
            + "&client_id=" + clientId + "&limit=15";

    apiUtils.fetchHttp(url, responseString -> {
      try {
        // Parse response into array of playlists
        JSONObject obj = new JSONObject(responseString);
        JSONArray playlistsDataArray = obj.getJSONArray("collection");
        List<Playlist> playlistsArray = new ArrayList<>();
        for (int i = 0; i < playlistsDataArray.length(); i++) {
          JSONObject playlistData = (JSONObject) playlistsDataArray.get(i);
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

          JSONObject artistData = playlistData.getJSONObject("user");
          String artistName = artistData.getString("username");
          int artistId = artistData.getInt("id");
          String artistAvatarUrl = artistData.getString("avatar_url");

          playlistsArray.add(new Playlist(coverUrl, title, duration, songCount, id,
                  new PartialArtist(artistId, artistName, artistAvatarUrl)));
        }

        PlaylistCollection searchedPlaylists = new PlaylistCollection(
                playlistsArray,
                obj.getInt("total_results"),
                obj.has("next_href")
                        ? obj.getString("next_href")
                        : null
        );

        consumer.accept(searchedPlaylists);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    });
  }

  public void getNextPlaylists(String nextUrl, Consumer<PlaylistCollection> consumer) {
    // Construct endpoint url
    String url = nextUrl + "&client_id=" + clientId;

    apiUtils.fetchHttp(url, responseString -> {
      try {
        // Parse response into array of playlists
        JSONObject obj = new JSONObject(responseString);
        JSONArray playlistsDataArray = obj.getJSONArray("collection");
        List<Playlist> playlistsArray = new ArrayList<>();
        for (int i = 0; i < playlistsDataArray.length(); i++) {
          JSONObject playlistData = (JSONObject) playlistsDataArray.get(i);
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

          JSONObject artistData = playlistData.getJSONObject("user");
          String artistName = artistData.getString("username");
          int artistId = artistData.getInt("id");
          String artistAvatarUrl = artistData.getString("avatar_url");

          playlistsArray.add(new Playlist(coverUrl, title, duration, songCount, id,
                  new PartialArtist(artistId, artistName, artistAvatarUrl)));
        }

        PlaylistCollection searchedPlaylists = new PlaylistCollection(
                playlistsArray,
                obj.getInt("total_results"),
                obj.has("next_href")
                        ? obj.getString("next_href")
                        : null
        );

        consumer.accept(searchedPlaylists);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    });
  }

  public void searchTracks(String query, Consumer<SongCollection> consumer) {
    // Construct endpoint url
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/tracks?q=" + query + "&client_id=" + clientId
            + "&limit=15";

    apiUtils.fetchHttp(url, responseString -> {
      try {
        // Parse response into array of songs
        JSONObject obj = new JSONObject(responseString);
        JSONArray songsDataArray = obj.getJSONArray("collection");
        List<Song> songsArray = new ArrayList<>();
        for (int i = 0; i < songsDataArray.length(); i++) {
          JSONObject songData = (JSONObject) songsDataArray.get(i);
          songsArray.add(ApiParser.parseSong(songData));
        }

        SongCollection searchedTracks = new SongCollection(
                songsArray,
                obj.getInt("total_results"),
                obj.has("next_href")
                        ? obj.getString("next_href")
                        : null
        );

        consumer.accept(searchedTracks);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    });
  }

  public void getNextTracks(String nextUrl, Consumer<SongCollection> consumer) {
    // Construct endpoint url
    String url = nextUrl + "&client_id=" + clientId;

    apiUtils.fetchHttp(url, responseString -> {
      try  {
        // Parse response into array of songs
        JSONObject obj = new JSONObject(responseString);
        JSONArray songsDataArray = obj.getJSONArray("collection");
        List<Song> songsArray = new ArrayList<>();
        for (int i = 0; i < songsDataArray.length(); i++) {
          JSONObject songData = (JSONObject) songsDataArray.get(i);
          songsArray.add(ApiParser.parseSong(songData));
        }

        SongCollection searchedTracks = new SongCollection(
                songsArray,
                obj.getInt("total_results"),
                obj.has("next_href")
                        ? obj.getString("next_href")
                        : null
        );

        consumer.accept(searchedTracks);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * get search suggestions for a given query
   *
   * @param query query to get search suggestions for
   * @returns list containing search suggestions
   */
  public void getSearchSuggestions(String query, Consumer<List<String>> consumer) {
    // Construct endpoint url
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/queries?q=" + query + "&client_id=" + clientId;

    apiUtils.fetchHttp(url, responseString -> {
      try {
        // Parse response into array of search suggestion
        List<String> searchSuggestions = new ArrayList<>();
        JSONObject obj = new JSONObject(responseString);
        JSONArray searchSuggestionsArray = obj.getJSONArray("collection");
        for (int i = 0; i < searchSuggestionsArray.length(); i++) {
          searchSuggestions.add((String) ((JSONObject) searchSuggestionsArray.get(i)).get("query"));
        }

        // Execute callback with requested data
        consumer.accept(searchSuggestions);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Get Client ID for Soundcloud API based on discovered pattern
   * 
   * @return Soundcloud API Client ID
   */
  private void setClientId() {
    // Fetch Soundcloud home page
    apiUtils.fetchHttp("https://soundcloud.com", responseString -> {
      // Get all the js file URLs found that contains the file URL with the Client ID
      String regex = "(https://a-v2\\.sndcdn\\.com/assets/)(.*?)(\\.js)";
      List<String> matches = new ArrayList<>();
      Matcher matcher = Pattern.compile(regex).matcher(responseString);
      while (matcher.find()) {
        matches.add(matcher.group());
      }

      // file URL containing Client ID is the last match
      String clientIdFileUrl = matches.get(matches.size() - 1);

      apiUtils.fetchHttp(clientIdFileUrl, responseString2 -> {
        // Match the values in any key-value pair with key being client_id
        String regex2 = "(client_id:\")(.*?)(\")";
        Matcher matcher2 = Pattern.compile(regex2).matcher(responseString2);

        // The one that is most frequently matched is known to be the Client ID
        // Hence, determine most frequently matched id
        HashMap<String, Integer> occuranceCountMap = new HashMap<>();
        String mostCommonOccurance = "";
        int maxOccurances = 0;

        while (matcher2.find()) {
          String id = matcher2.group(2);
          Optional<Integer> occuranceCount = Optional.ofNullable(occuranceCountMap.get(id));
          int newOccuranceCount = !occuranceCount.isPresent() ? 1 : occuranceCount.get() + 1;
          occuranceCountMap.put(id, newOccuranceCount);

          if (newOccuranceCount > maxOccurances) {
            maxOccurances = newOccuranceCount;
            mostCommonOccurance = id;
          }
        }

        // Set Client ID to the most frequently matched
        clientId = mostCommonOccurance;
        Log.d("API_WRAPPER", clientId == ""
                ? "ERROR: Client ID could not be fetched"
                : "DEBUG: Client ID found: " + clientId);
      });
    }, error -> {
      Log.e("API_WRAPPER", "No Connection!");
    });
  }
}
