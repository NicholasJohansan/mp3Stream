package co.carrd.njportfolio.mp3stream.Search.Classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;

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
        }
        View songResultView = inflater.inflate(R.layout.item_song_result, parent, false);
        return new SongResultViewHolder(songResultView);
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

    private static void loadViewholderImage(View itemView, ImageView imageView, String imageUrl) {

        // Create placeholder progressbar while loading image
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(itemView.getContext());
        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.gray, null));
        progressDrawable.setColorFilter(ResourcesCompat.getColor(itemView.getResources(), R.color.orange, null), PorterDuff.Mode.SCREEN);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(40f);
        progressDrawable.start();

        // Load with glide
        imageUrl = imageUrl == null ? "" : imageUrl;
        Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .placeholder(progressDrawable)
                .override(Target.SIZE_ORIGINAL)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.light_gray, null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.light_gray, null));
                        return false;
                    }
                })
                .into(imageView);
    }

    public class SongResultViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameTextView;
        private TextView artistTextView;
        private TextView durationTextView;
        private ImageView coverImageView;
        private View itemView;

        public SongResultViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_result_item_name);
            artistTextView = itemView.findViewById(R.id.song_result_item_artist);
            durationTextView = itemView.findViewById(R.id.song_result_item_duration);
            coverImageView = itemView.findViewById(R.id.song_result_item_cover);
            this.itemView = itemView;
        }

        public void bindSong(Song song) {

            songNameTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist().getName());
            durationTextView.setText(song.getFriendlyDuration());

            loadViewholderImage(itemView, coverImageView, song.getCoverUrl());


        }
    }

    public class PlaylistResultViewHolder extends RecyclerView.ViewHolder {
        private TextView playlistNameTextView;
        private TextView songCountTextView;
        private TextView durationTextView;
        private ImageView coverImageView;
        private View itemView;

        public PlaylistResultViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistNameTextView = itemView.findViewById(R.id.playlist_result_item_name);
            songCountTextView = itemView.findViewById(R.id.playlist_result_item_track_count);
            durationTextView = itemView.findViewById(R.id.playlist_result_item_duration);
            coverImageView = itemView.findViewById(R.id.playlist_result_item_cover);
            this.itemView = itemView;
        }

        public void bindPlaylist(Playlist playlist) {

            playlistNameTextView.setText(playlist.getTitle());
            songCountTextView.setText(String.valueOf(playlist.getSongCount()) + " Songs");
            durationTextView.setText(playlist.getFriendlyDuration());

            loadViewholderImage(itemView, coverImageView, playlist.getCoverUrl());


        }
    }
}
