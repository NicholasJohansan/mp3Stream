package co.carrd.njportfolio.mp3stream.Player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class MiniPlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;

    private ImageView songCoverImageView;
    private TextView songNameTextView;
    private TextView artistNameTextView;
    private ImageButton playPauseButton;

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
                songCoverImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_background, null));
                songNameTextView.setText("No Song Selected");
                artistNameTextView.setVisibility(View.GONE);
            } else {
                UiUtils.loadImage(songCoverImageView, song.getCoverUrl());
                songNameTextView.setText(song.getTitle());
                songNameTextView.setSelected(true);
                artistNameTextView.setText(song.getArtist().getName());
                artistNameTextView.setVisibility(View.VISIBLE);
            }
        });
        playerViewModel.getIsPlaying().observeForever(isPlaying -> {
            if (isPlaying) {
                playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_player_pause, null));
            } else {
                playPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_player_play, null));
            }
        });

        // Set up play/pause button
        playPauseButton.setOnClickListener(v -> playerFragment.playPause());
    }
}
