package co.carrd.njportfolio.mp3stream.Player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.exoplayer2.ExoPlayer;

import co.carrd.njportfolio.mp3stream.Library.LibraryViewModel;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiUtils;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class MiniPlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;
    private LibraryViewModel libraryViewModel;

    private ImageView songCoverImageView;
    private TextView songNameTextView;
    private TextView artistNameTextView;
    private ImageButton playPauseButton;
    private ProgressBar loadingView;
    private ProgressBar progressBar;
    private TextView noSongLabel;
    private ImageButton likeButton;

    private PlayerFragment playerFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_mini_player, container, false);

        // Link UI
        songCoverImageView = fragmentView.findViewById(R.id.mini_player_song_cover);
        songNameTextView = fragmentView.findViewById(R.id.mini_player_song_name);
        artistNameTextView = fragmentView.findViewById(R.id.mini_player_artist_name);
        playPauseButton = fragmentView.findViewById(R.id.mini_player_play_pause_button);
        loadingView = fragmentView.findViewById(R.id.mini_player_loading_view);
        progressBar = fragmentView.findViewById(R.id.mini_player_progress_bar);
        noSongLabel = fragmentView.findViewById(R.id.mini_player_no_song_label);
        likeButton = fragmentView.findViewById(R.id.mini_player_like_button);

        // Link player fragment
        playerFragment = PlayerFragment.getInstance();

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate ViewModels
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        libraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        // Set up observer
        playerViewModel.getCurrentSong().observeForever(song -> {
            if (song == null) {
                noSongLabel.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.GONE);
                playPauseButton.setVisibility(View.GONE);
                songCoverImageView.setVisibility(View.GONE);
                songNameTextView.setVisibility(View.GONE);
                artistNameTextView.setVisibility(View.GONE);
            } else {
                UiUtils.loadImage(songCoverImageView, song.getCoverUrl());
                songNameTextView.setText(song.getTitle());
                artistNameTextView.setText(song.getArtist().getName());

                noSongLabel.setVisibility(View.GONE);
                likeButton.setVisibility(View.VISIBLE);
                songCoverImageView.setVisibility(View.VISIBLE);
                songNameTextView.setVisibility(View.VISIBLE);
                artistNameTextView.setVisibility(View.VISIBLE);

                if (!playerViewModel.getIsLoading().getValue()) {
                    playPauseButton.setVisibility(View.VISIBLE);
                }

                songNameTextView.setSelected(true); // Makes sure that marquee effect works
            }
        });
        playerViewModel.getIsPlaying().observeForever(isPlaying -> {
            if (isPlaying) {
                playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_player_pause, null));
            } else {
                playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_player_play, null));
            }
        });
        playerViewModel.getIsLoading().observeForever(isLoading -> {
            Song currentSong = playerViewModel.getCurrentSong().getValue();
            if (isLoading) {
                loadingView.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.INVISIBLE);
            } else {
                loadingView.setVisibility(View.GONE);
                if (playerViewModel.getCurrentSong().getValue() != null) {
                    likeButton.setVisibility(View.VISIBLE);
                    playPauseButton.setVisibility(View.VISIBLE);
                }
            }
        });
        playerViewModel.getIsLiked().observeForever(songIsLiked -> {
            likeButton.setImageResource(songIsLiked
                    ? R.drawable.icon_song_liked
                    : R.drawable.icon_song_not_liked);
        });

        // Set up play/pause button
        playPauseButton.setOnClickListener(v -> playerFragment.playPause());

        // Set up like button
        likeButton.setOnClickListener(v -> {
            libraryViewModel.toggleLike(playerViewModel.getCurrentSong().getValue().getId());
        });
    }

    public void updateProgress(ExoPlayer player) {
        int elapsedTime = (int) player.getCurrentPosition();
        int duration = (int) player.getDuration();
        int bufferedDuration = (int) player.getBufferedPosition();
        progressBar.setMax(duration);
        progressBar.setProgress(elapsedTime);
        progressBar.setSecondaryProgress(bufferedDuration);
    }
}
