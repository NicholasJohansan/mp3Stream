package co.carrd.njportfolio.mp3stream.SoundcloudApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

public class ApiUtils {
    private static OkHttpClient httpClient;

    public ApiUtils(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void fetchPlaylistCollection(String url, Consumer<PlaylistCollection> playlistCollectionConsumer) {
        fetchHttp(url, responseString -> {
            PlaylistCollection searchedPlaylists = ApiParser.parsePlaylistCollection(responseString);
            playlistCollectionConsumer.accept(searchedPlaylists);
        });
    }

    public void fetchSongCollection(String url, Consumer<SongCollection> songCollectionConsumer) {
        fetchHttp(url, responseString -> {
            SongCollection searchedTracks = ApiParser.parseSongCollection(responseString);
            songCollectionConsumer.accept(searchedTracks);
        });
    }

    public void fetchHttp(String url, Consumer<String> responseStringConsumer) {
        fetchHttp(url, responseStringConsumer, null);
    }

    public void fetchHttp(String url, Consumer<String> responseStringConsumer, @Nullable Consumer<IOException> errorConsumer) {
        httpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (errorConsumer != null) {
                    errorConsumer.accept(e);
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) throw new IOException("Unexpected Code: " + response);
                String bodyString = responseBody.string();
                responseStringConsumer.accept(bodyString);
            }
        });
    }

    public static String getFriendlyDuration(int duration) {
        duration /= 100;
        int seconds = duration % 60;
        int minutes = duration / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
