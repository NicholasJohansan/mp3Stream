package co.carrd.njportfolio.mp3stream.Search.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.R;
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
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View songResultView = inflater.inflate(R.layout.item_song_result, parent, false);
        if (viewType == 0) {
            return new SongResultViewHolder(songResultView);
        }
        return new SongResultViewHolder(songResultView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = holder.getItemViewType();
        if (itemViewType == 0) {
            Song song = (Song) searchResults.getValue().get(position);
            ((SongResultViewHolder) holder).bindSong(song);
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
//        private ImageView ivCoverArt;
        private View itemView;

        public SongResultViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_result_item_name);
            artistTextView = itemView.findViewById(R.id.song_result_item_artist);
            durationTextView = itemView.findViewById(R.id.song_result_item_duration);
//            ivCoverArt = itemView.findViewById(R.id.itemSongResultCoverImg);
            this.itemView = itemView;
        }

        public void bindSong(Song song) {
            songNameTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist().getName());
            durationTextView.setText(song.getFriendlyDuration());
//            String coverArtUrl = song.getCoverUrl();
//            System.out.println(song.getTitle() + " " + coverArtUrl);
//            coverArtUrl = coverArtUrl == null ? "" : coverArtUrl;
//            Glide.with(itemView)
//                    .load(coverArtUrl)
//                    .error(R.drawable.ic_baseline_error_24)
//                    .override(Target.SIZE_ORIGINAL)
//                    .fitCenter()
//                    .into(ivCoverArt);
//            ivCoverArt.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
