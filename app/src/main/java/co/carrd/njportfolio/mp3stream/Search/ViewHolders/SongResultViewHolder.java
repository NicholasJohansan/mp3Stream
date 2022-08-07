package co.carrd.njportfolio.mp3stream.Search.ViewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.Library.LibraryFragment;
import co.carrd.njportfolio.mp3stream.Library.LibraryViewModel;
import co.carrd.njportfolio.mp3stream.Library.LikedPlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.Player.PlayerFragment;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Details.PlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SongResultViewHolder extends RecyclerView.ViewHolder {
    private TextView songNameTextView;
    private TextView artistTextView;
    private TextView durationTextView;
    private ImageView coverImageView;
    private ConstraintLayout georestrictedView;
    private ImageButton playButton;
    private ImageButton likeButton;

    private Fragment parentFragment;
    private LibraryViewModel libraryViewModel;

    public SongResultViewHolder(@NonNull View itemView, @NonNull Fragment parentFragment) {
        super(itemView);
        songNameTextView = itemView.findViewById(R.id.song_result_item_name);
        artistTextView = itemView.findViewById(R.id.song_result_item_artist);
        durationTextView = itemView.findViewById(R.id.song_result_item_duration);
        coverImageView = itemView.findViewById(R.id.song_result_item_cover);
        georestrictedView = itemView.findViewById(R.id.song_result_georestricted_view);
        playButton = itemView.findViewById(R.id.song_result_play_button);
        likeButton = itemView.findViewById(R.id.song_result_like_button);

        libraryViewModel = new ViewModelProvider(parentFragment.requireActivity()).get(LibraryViewModel.class);
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

        playButton.setOnClickListener(view -> {
            String simpleName = parentFragment.getClass().getSimpleName();
            if (simpleName.equals(PlaylistDetailsFragment.class.getSimpleName())) {
                // Play as playlist
                ((PlaylistDetailsFragment) parentFragment).playPlaylist(getLayoutPosition());
            } else if (simpleName.equals(LikedPlaylistDetailsFragment.class.getSimpleName())) {
                ((LikedPlaylistDetailsFragment) parentFragment).playPlaylist(getLayoutPosition());
            } else {
                // Play as a single song
                PlayerFragment.getInstance().setSong(song);
            }
        });

        libraryViewModel.getLikedSongsIdList().observe(parentFragment, songsIdList -> {
            likeButton.setImageResource(libraryViewModel.songIsLiked(song.getId())
                    ? R.drawable.icon_song_liked
                    : R.drawable.icon_song_not_liked);
        });

        likeButton.setOnClickListener(v -> {
            libraryViewModel.toggleLike(song.getId());
        });

        UiUtils.loadImage(coverImageView, song.getCoverUrl());
    }
}