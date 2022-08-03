package co.carrd.njportfolio.mp3stream.Search.Classes;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.PlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.Search.TestFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Artist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MutableLiveData<List<Object>> searchResults;
    private String nextUrl;

    public SearchResultsAdapter() {
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
            return new SongResultViewHolder(songResultView);
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

//    private Bitmap getBitmap(String imageUrl) {
//        return BitmapFactory.decodeStream((new URL(imageUrl)).openConnection().getInputStream());
//    }

    public class SongResultViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameTextView;
        private TextView artistTextView;
        private TextView durationTextView;
        private ImageView coverImageView;

        public SongResultViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_result_item_name);
            artistTextView = itemView.findViewById(R.id.song_result_item_artist);
            durationTextView = itemView.findViewById(R.id.song_result_item_duration);
            coverImageView = itemView.findViewById(R.id.song_result_item_cover);
        }

        public void bindSong(Song song) {

            songNameTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist().getName());
            durationTextView.setText(song.getFriendlyDuration());

            itemView.setOnClickListener(view -> {
                Fragment newFragment = new TestFragment();
                Bundle args = new Bundle();
                args.putInt("num", 1);
                newFragment.setArguments(args);
                SearchFragment.getInstance().addFragmentToBackStack(newFragment);
            });

            UiUtils.loadImage(coverImageView, song.getCoverUrl());


        }
    }

    public class PlaylistResultViewHolder extends RecyclerView.ViewHolder {
        private TextView playlistNameTextView;
        private TextView artistTextView;
        private TextView songsDurationTextView;
        private ImageView coverImageView;

        public PlaylistResultViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistNameTextView = itemView.findViewById(R.id.playlist_result_item_name);
            artistTextView = itemView.findViewById(R.id.playlist_result_item_artist);
            songsDurationTextView = itemView.findViewById(R.id.playlist_result_item_songs_duration);
            coverImageView = itemView.findViewById(R.id.playlist_result_item_cover);
        }

        public void bindPlaylist(Playlist playlist) {

            playlistNameTextView.setText(playlist.getTitle());
            artistTextView.setText(playlist.getArtist().getName());
            songsDurationTextView.setText(playlist.getSongCount() + " Songs ⋅ " + playlist.getFriendlyDuration());

            UiUtils.loadImage(coverImageView, playlist.getCoverUrl());

            itemView.setOnClickListener(view -> {
                Fragment playlistDetailsFragment = new PlaylistDetailsFragment();
                Bundle args = new Bundle();
                args.putParcelable("playlist", playlist);
                playlistDetailsFragment.setArguments(args);
                SearchFragment.getInstance().addFragmentToBackStack(playlistDetailsFragment);
            });

        }
    }

    public class ArtistResultViewHolder extends RecyclerView.ViewHolder {
        private TextView artistNameTextView;
        private TextView songCountTextView;
        private TextView playlistCountTextView;
        private ImageView avatarImageView;

        public ArtistResultViewHolder(@NonNull View itemView) {
            super(itemView);
            artistNameTextView = itemView.findViewById(R.id.artist_result_item_name);
            songCountTextView = itemView.findViewById(R.id.artist_result_item_song_count);
            playlistCountTextView = itemView.findViewById(R.id.artist_result_item_playlist_count);
            avatarImageView = itemView.findViewById(R.id.artist_result_item_avatar);
        }

        public void bindArtist(Artist artist) {

            artistNameTextView.setText(artist.getName());
            songCountTextView.setText(artist.getSongCount() + " Songs");
            playlistCountTextView.setText(artist.getPlaylistCount() + " Playlists");

            UiUtils.loadImage(avatarImageView, artist.getAvatarUrl());


        }
    }
}
