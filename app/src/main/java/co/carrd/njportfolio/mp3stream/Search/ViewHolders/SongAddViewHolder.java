package co.carrd.njportfolio.mp3stream.Search.ViewHolders;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.Library.LibraryFragment;
import co.carrd.njportfolio.mp3stream.Library.LibraryViewModel;
import co.carrd.njportfolio.mp3stream.Library.LikedPlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.Player.PlayerFragment;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Details.PlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SongAddViewHolder extends RecyclerView.ViewHolder {
    private TextView songNameTextView;
    private TextView artistTextView;
    private TextView durationTextView;
    private ImageView coverImageView;
    private ConstraintLayout georestrictedView;
    private ImageButton addButton;

    private Fragment parentFragment;
    private LibraryViewModel libraryViewModel;

    public SongAddViewHolder(@NonNull View itemView, @NonNull Fragment parentFragment) {
        super(itemView);
        songNameTextView = itemView.findViewById(R.id.song_result_item_name);
        artistTextView = itemView.findViewById(R.id.song_result_item_artist);
        durationTextView = itemView.findViewById(R.id.song_result_item_duration);
        coverImageView = itemView.findViewById(R.id.song_result_item_cover);
        georestrictedView = itemView.findViewById(R.id.song_result_georestricted_view);
        addButton = itemView.findViewById(R.id.song_result_add_button);

        libraryViewModel = LibraryFragment.getInstance().getLibraryViewModel();
        this.parentFragment = parentFragment;
    }

    public void bindSong(Song song) {

        songNameTextView.setText(song.getTitle());
        artistTextView.setText(song.getArtist().getName());
        durationTextView.setText(song.getFriendlyDuration());

        if (song.getPartialStreamUrl() == null) {
            georestrictedView.setVisibility(View.VISIBLE);
        } else {
            georestrictedView.setVisibility(View.GONE);
        }

        libraryViewModel.getSavedSongsIdList().observe(parentFragment, songsIdList -> {
            addButton.setImageResource(libraryViewModel.getSongIsSaved(song.getId())
                    ? R.drawable.icon_song_remove
                    : R.drawable.icon_song_add);
        });

        addButton.setOnClickListener(v -> {
            libraryViewModel.toggleSave(song.getId());
        });

        UiUtils.loadImage(coverImageView, song.getCoverUrl());
    }
}