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

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class MiniPlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;

    private ImageView songCoverImageView;
    private TextView songNameTextView;
    private TextView artistNameTextView;
    private ImageButton playPauseButton;
    private ProgressBar loadingView;
    private TextView noSongLabel;

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
        noSongLabel = fragmentView.findViewById(R.id.mini_player_no_song_label);

        // Link player fragment
        playerFragment = PlayerFragment.getInstance();

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate PlayerViewModel
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        // Set up observer
        playerViewModel.getCurrentSong().observeForever(song -> {
            if (song == null) {
                noSongLabel.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.GONE);
                songCoverImageView.setVisibility(View.GONE);
                songNameTextView.setVisibility(View.GONE);
                artistNameTextView.setVisibility(View.GONE);
            } else {
                UiUtils.loadImage(songCoverImageView, song.getCoverUrl());
                songNameTextView.setText(song.getTitle());
                artistNameTextView.setText(song.getArtist().getName());

                noSongLabel.setVisibility(View.GONE);
                playPauseButton.setVisibility(View.VISIBLE);
                songCoverImageView.setVisibility(View.VISIBLE);
                songNameTextView.setVisibility(View.VISIBLE);
                artistNameTextView.setVisibility(View.VISIBLE);

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
                playPauseButton.setVisibility(View.GONE);
            } else {
                loadingView.setVisibility(View.GONE);
                if (playerViewModel.getCurrentSong().getValue() != null) {
                    playPauseButton.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up play/pause button
        playPauseButton.setOnClickListener(v -> playerFragment.playPause());
    }
}
