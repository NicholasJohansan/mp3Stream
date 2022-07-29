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

import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.ArtistCollection;
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
      initialiseClientId();
    }
  }

  public void searchArtists(String query, Consumer<ArtistCollection> consumer) {
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/users?q=" + query
            + "&client_id=" + clientId + "&limit=15";
    apiUtils.fetchArtistCollection(url, consumer);
  }

  public void getNextArtists(String nextUrl, Consumer<ArtistCollection> consumer) {
    String url = nextUrl + "&client_id=" + clientId;
    apiUtils.fetchArtistCollection(url, consumer);
  }

  public void searchAlbums(String query, Consumer<PlaylistCollection> consumer) {
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/albums?q=" + query
            + "&client_id=" + clientId + "&limit=15";
    // Albums are just playlists
    apiUtils.fetchPlaylistCollection(url, consumer);
  }

  public void getNextAlbums(String nextUrl, Consumer<PlaylistCollection> consumer) {
    String url = nextUrl + "&client_id=" + clientId;
    // Albums are just playlists
    apiUtils.fetchPlaylistCollection(url, consumer);
  }

  public void searchPlaylists(String query, Consumer<PlaylistCollection> consumer) {
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/playlists_without_albums?q=" + query
            + "&client_id=" + clientId + "&limit=15";
    apiUtils.fetchPlaylistCollection(url, consumer);
  }

  public void getNextPlaylists(String nextUrl, Consumer<PlaylistCollection> consumer) {
    String url = nextUrl + "&client_id=" + clientId;
    apiUtils.fetchPlaylistCollection(url, consumer);
  }

  public void searchTracks(String query, Consumer<SongCollection> consumer) {
    query = Uri.encode(query);
    String url = "https://api-v2.soundcloud.com/search/tracks?q=" + query + "&client_id=" + clientId
            + "&limit=15";
    apiUtils.fetchSongCollection(url, consumer);
  }

  public void getNextTracks(String nextUrl, Consumer<SongCollection> consumer) {
    String url = nextUrl + "&client_id=" + clientId;
    apiUtils.fetchSongCollection(url, consumer);
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
      List<String> searchSuggestions = ApiParser.parseSearchSuggestions(responseString);
      consumer.accept(searchSuggestions);
    });
  }

  /**
   * Initialise the client id for Soundcloud API by finding one on Soundcloud Site
   * This finds the client id based on discovered pattern and initialises it
   */
  private void initialiseClientId() {
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
