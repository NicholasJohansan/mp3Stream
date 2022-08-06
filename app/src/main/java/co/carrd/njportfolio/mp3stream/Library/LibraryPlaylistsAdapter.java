package co.carrd.njportfolio.mp3stream.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.ViewHolders.PlaylistResultViewHolder;

public class LibraryPlaylistsAdapter extends RecyclerView.Adapter<LibraryPlaylistsAdapter.LibraryPlaylistViewHolder> {
    private LibraryFragment libraryFragment;

    public LibraryPlaylistsAdapter(LibraryFragment libraryFragment) {
        this.libraryFragment = libraryFragment;
    }

    @NonNull
    @Override
    public LibraryPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_library_playlist, parent, false);
        return new LibraryPlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryPlaylistViewHolder holder, int position) {
        holder.bindPosition(position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class LibraryPlaylistViewHolder extends RecyclerView.ViewHolder {

        private ImageView playlistCover;
        private ImageButton moreButton;
        private TextView nameTextView;
        private TextView songsCountTextView;

        public LibraryPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link UI
            playlistCover = itemView.findViewById(R.id.item_library_playlist_cover);
            nameTextView = itemView.findViewById(R.id.item_library_playlist_name);
            songsCountTextView = itemView.findViewById(R.id.item_library_playlist_songs_count);
        }

        public void bindPosition(int position) {
            if (position == 0) {
                // Liked Playlist
                nameTextView.setText("Liked Songs");
                playlistCover.setImageResource(R.drawable.liked_playlist_drawable);
                libraryFragment.getLibraryViewModel().getLikedSongsIdList().observe(libraryFragment, newSongIdsList -> {
                    songsCountTextView.setText(newSongIdsList.size() + " Songs");
                });
            }
        }
    }
}
