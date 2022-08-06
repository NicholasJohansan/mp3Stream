package co.carrd.njportfolio.mp3stream.Search.Results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.ViewHolders.ArtistResultViewHolder;
import co.carrd.njportfolio.mp3stream.Search.ViewHolders.PlaylistResultViewHolder;
import co.carrd.njportfolio.mp3stream.Search.ViewHolders.SongResultViewHolder;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Artist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MutableLiveData<List<Object>> searchResults;
    private String nextUrl;

    private Fragment parentFragment;

    public SearchResultsAdapter(Fragment parent) {
        parentFragment = parent;
        searchResults = new MutableLiveData<>();
        searchResults.setValue(new ArrayList<>());
    }

    @Override
    public int getItemViewType(int position) {
        Object searchResultClass = searchResults.getValue().get(position).getClass().getSimpleName();
        if (searchResultClass.equals(Song.class.getSimpleName())) {
            return 0;
        } else if (searchResultClass.equals(Playlist.class.getSimpleName())) {
            return 1;
        } else if (searchResultClass.equals(Artist.class.getSimpleName())) {
            return 2;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 0) {
            View songResultView = inflater.inflate(R.layout.item_song_result, parent, false);
            return new SongResultViewHolder(songResultView, parentFragment);
        } else if (viewType == 1) {
            View playlistResultView = inflater.inflate(R.layout.item_playlist_result, parent, false);
            return new PlaylistResultViewHolder(playlistResultView);
        } else if (viewType == 2) {
            View artistResultView = inflater.inflate(R.layout.item_artist_result, parent, false);
            return new ArtistResultViewHolder(artistResultView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = holder.getItemViewType();
        if (itemViewType == 0) {
            Song song = (Song) searchResults.getValue().get(position);
            ((SongResultViewHolder) holder).bindSong(song);
        } else if (itemViewType == 1) {
            Playlist playlist = (Playlist) searchResults.getValue().get(position);
            ((PlaylistResultViewHolder) holder).bindPlaylist(playlist);
        } else if (itemViewType == 2) {
            Artist artist = (Artist) searchResults.getValue().get(position);
            ((ArtistResultViewHolder) holder).bindArtist(artist);
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.getValue().size();
    }

    public void addSearchResults(List<Object> newSearchResults) {
        searchResults.getValue().addAll(newSearchResults);
    }
    public MutableLiveData<List<Object>> getSearchResults() {
        return searchResults;
    }
    public String getNextUrl() { return nextUrl; };
    public void setNextUrl(String nextUrl) { this.nextUrl = nextUrl; }
}
