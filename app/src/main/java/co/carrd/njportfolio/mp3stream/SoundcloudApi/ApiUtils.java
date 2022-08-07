package co.carrd.njportfolio.mp3stream.SoundcloudApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.ArtistCollection;
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
    public static int PAGE_SIZE = 15;

    public ApiUtils(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void fetchPlaylistTracksList(String url, int[] trackIdsOrder, Consumer<List<Song>> playlistTracksListConsumer) {
        fetchHttp(url, responseString -> {
            List<Song> playlistSongs = ApiParser.parseSongList(responseString);
            List<Song> orderedSongs = new ArrayList<>();
            for (int trackId : trackIdsOrder) {
                Optional<Song> foundSong = playlistSongs.stream().filter(song -> song.getId() == trackId).findFirst();
                orderedSongs.add(foundSong.get());
            }
            playlistTracksListConsumer.accept(orderedSongs);
        });
    }

    public void fetchArtistCollection(String url, Consumer<ArtistCollection> artistCollectionConsumer) {
        fetchHttp(url, responseString -> {
            ArtistCollection searchedArtists = ApiParser.parseArtistCollection(responseString);
            artistCollectionConsumer.accept(searchedArtists);
        });
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

    public static String fetchSyncResponseString(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();
        String responseString = response.body().string();
        return responseString;
    }

    public static int[] paginateIds(int page, int[] ids) {
        return paginateIds(page, ids, PAGE_SIZE);
    }

    public static int[] paginateIds(int page, int[] ids, int pageSize) {
        page -= 1;
        int startIndex = page * pageSize;
        if (startIndex >= ids.length) return null;
        int endIndex = Math.min(startIndex + pageSize, ids.length);
        return Arrays.copyOfRange(ids, startIndex, endIndex);
    }

    public static String getFriendlyDuration(int duration) {
        duration /= 1000;
        int seconds = duration % 60;
        int minutes = duration / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Initialise variables used for managing just-in-time stream resolution with updated auth params
    private static Map<String, String> streamIdToUrlMap = new HashMap<>();
    private static Pattern streamIdPattern = Pattern.compile("(/playlist/)(.*)\\.128\\.mp3(/)");
    private static Pattern streamChunkIdPattern = Pattern.compile("(/\\d+/)(\\d+/)(\\d+/)(.*)\\.128\\.mp3");
    private static Pattern urlPattern = Pattern.compile("(https)(.*)");

    private static String findStreamChunkId(String data) {
        Matcher matcher = streamChunkIdPattern.matcher(data);
        matcher.find();
        return matcher.group(4);
    }

    private static String findStreamId(String data) {
        Matcher matcher = streamIdPattern.matcher(data);
        matcher.find();
        return matcher.group(2);
    }

    private static String findUrl(String data) {
        Matcher matcher = urlPattern.matcher(data);
        matcher.find();
        return matcher.group(0);
    }

    public static String getStreamFileUrl(String partialStreamUrl, String clientId) throws IOException {
        String url = partialStreamUrl + "?client_id=" + clientId;
        String responseString = fetchSyncResponseString(url);
        String mainStreamUrl = ApiParser.parseStreamUrl(responseString);
        return mainStreamUrl;
    }

    private static String getStreamAuthParams(String streamId, String clientId) throws IOException {
        // Get the main HLS stream file url
        String partialStreamUrl = streamIdToUrlMap.get(streamId);
        String mainStreamUrl = getStreamFileUrl(partialStreamUrl, clientId);
        // Fetch the main HLS stream file
        String responseString = fetchSyncResponseString(mainStreamUrl);
        String foundChunkUrl = findUrl(responseString);
        String authParams = "?" + foundChunkUrl.split("\\?")[1]; // because url params are behind ?
        return authParams;
    }

    public static String resolveStreamChunkUrl(String streamChunkUrl, String clientId) throws IOException {
        String streamChunkId = findStreamChunkId(streamChunkUrl);
        String authParams = getStreamAuthParams(streamChunkId, clientId);
        streamChunkUrl = streamChunkUrl.split("\\?")[0]; // Drop url params for new auth params
        return streamChunkUrl + authParams;
    }

    public static void registerStreamId(String streamUrl, String partialStreamUrl) {
        String streamId = findStreamId(streamUrl);
        if (!streamIdToUrlMap.containsKey(streamId)) {
            streamIdToUrlMap.put(streamId, partialStreamUrl);
        }
    }
}
