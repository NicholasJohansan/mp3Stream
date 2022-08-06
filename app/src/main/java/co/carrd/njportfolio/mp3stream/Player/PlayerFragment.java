package co.carrd.njportfolio.mp3stream.Player;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.colorgreen.swiper.OnSwipeTouchListener;
import com.colorgreen.swiper.SwipeAction;
import com.colorgreen.swiper.SwipeActionListener;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiUtils;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class PlayerFragment extends Fragment {

    private MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();
    private ImageButton minimizeButton;
    private ImageView songCoverImageView;
    private TextView songNameTextView;
    private TextView artistNameTextView;
    private TextView elapsedTimeTextView;
    private TextView durationTextView;
    private SwipeAction swipeAction;
    private ImageView playPauseButton;
    private ProgressBar loadingView;
    private SeekBar seekBar;
    private ImageButton skipNextButton;
    private ImageButton skipPreviousButton;
    private ArrayList<Song> songsList;
    private ArrayList<String> streamUrlsList;

    private static PlayerFragment instance;
    private PlayerViewModel playerViewModel;
    private Handler seekBarHandler;
    private ExoPlayer player = MainApplication.getInstance().getPlayer();
    private ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();

    public static PlayerFragment getInstance() {
        if (instance == null) {
            instance = new PlayerFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_player, container, false);

        // Link UI
        minimizeButton = fragmentView.findViewById(R.id.player_minimize_button);
        songCoverImageView = fragmentView.findViewById(R.id.player_song_cover);
        songNameTextView = fragmentView.findViewById(R.id.player_song_name);
        artistNameTextView = fragmentView.findViewById(R.id.player_artist_name);
        elapsedTimeTextView = fragmentView.findViewById(R.id.player_elapsed_time_text_view);
        durationTextView = fragmentView.findViewById(R.id.player_total_time_text_view);
        playPauseButton = fragmentView.findViewById(R.id.player_play_pause_button);
        loadingView = fragmentView.findViewById(R.id.player_loading_view);
        seekBar = fragmentView.findViewById(R.id.player_seekbar);
        skipNextButton = fragmentView.findViewById(R.id.player_skip_next_button);
        skipPreviousButton = fragmentView.findViewById(R.id.player_skip_previous_button);

        // Get exoplayer

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inflate Mini Player Fragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.mini_player_fragment_view, miniPlayerFragment)
                .commit();

        // Set up handler for seekbar
        seekBarHandler = new Handler();

        // Set Up Swipe Action
        setUpSwipeAction();

        // Set Up Minimize Button
        minimizeButton.setOnClickListener(v -> swipeAction.collapse());

        // Instantiate PlayerViewModel
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        // Set up observer
        playerViewModel.getCurrentSong().observeForever(song -> {
            if (song == null) {
                swipeAction.setBlocked(true);
            } else {
                swipeAction.setBlocked(false);
                UiUtils.loadImage(songCoverImageView, song.getCoverUrl());
                songNameTextView.setText(song.getTitle());
                songNameTextView.setSelected(true);
                artistNameTextView.setText(song.getArtist().getName());
                durationTextView.setText(song.getFriendlyDuration());
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
            if (isLoading) {
                loadingView.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.INVISIBLE);
            } else {
                loadingView.setVisibility(View.GONE);
                if (playerViewModel.getCurrentSong().getValue() != null) {
                    playPauseButton.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up play/pause button on click listener
        playPauseButton.setOnClickListener(v -> playPause());

        // Set up skip next/previous button
        skipNextButton.setOnClickListener(v -> player.seekToNextMediaItem());
        skipPreviousButton.setOnClickListener(v -> player.seekToPreviousMediaItem());

        // Set listener on player
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                playerViewModel.getIsPlaying().setValue(isPlaying);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    playerViewModel.getIsLoading().setValue(true);
                    updateProgress();
                } else {
                    playerViewModel.getIsLoading().setValue(false);
                }

                if (playbackState == Player.STATE_ENDED) {
                    player.pause();
                }
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                if (mediaItem != null && mediaItem.localConfiguration.tag != null) {
                    SongMediaMetaData metaData = (SongMediaMetaData) mediaItem.localConfiguration.tag;
                    int index = metaData.getMediaIndex();
                    playerViewModel.getCurrentSong().setValue(songsList.get(index));
                }
            }
        });

        // Set up seekbar seeking
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private boolean startedTouch = false;
            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (startedTouch && fromUser) {
                    this.progress = progress;
                    elapsedTimeTextView.setText(ApiUtils.getFriendlyDuration(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startedTouch = true;
                seekBarHandler.removeCallbacks(updateProgressRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startedTouch = false;
                player.seekTo(progress);
                updateProgress();
            }
        });
    }

    public void playPause() {
        if(playerViewModel.getIsPlaying().getValue()) {
            // if it is playing then pause
            player.pause();
        } else {
            // if paused then play
            if (player.getCurrentPosition() >= player.getDuration()) {
                player.seekTo(0);
            }
            player.play();
        }
    }

    public void setSong(Song song) {
        playerViewModel.getCurrentSong().setValue(song);
        player.stop();
        player.clearMediaItems();
        player.setMediaItem(MediaItem.fromUri(song.getPartialStreamUrl()));
        player.prepare();
        player.play();
    }

    public void setPlaylist(List<Song> songsList, int startSongIndex) {
        this.songsList = (ArrayList<Song>) songsList;
        playerViewModel.getCurrentSong().setValue(songsList.get(startSongIndex));
        player.stop();
        player.clearMediaItems();
        playerViewModel.getIsLoading().setValue(true);
        for (int i = 0; i < songsList.size(); i++) {
            player.addMediaItem(new MediaItem.Builder()
                .setUri(songsList.get(i).getPartialStreamUrl())
                .setTag(new SongMediaMetaData(i))
                .build());
        }
        player.prepare();
        player.seekTo(startSongIndex, 0);
        player.play();
    }

    public class SongMediaMetaData {
        private int mediaIndex;

        public SongMediaMetaData(int mediaIndex) {
            this.mediaIndex = mediaIndex;
        }

        public int getMediaIndex() {
            return mediaIndex;
        }
    }

    // Code for updating progress bar

    private void updateProgress() {
        int elapsedTime = (int) player.getCurrentPosition();
        int duration = (int) player.getDuration();
        int bufferedDuration = (int) player.getBufferedPosition();
        seekBar.setMax(duration);
        seekBar.setProgress(elapsedTime);
        seekBar.setSecondaryProgress(bufferedDuration);
        elapsedTimeTextView.setText(ApiUtils.getFriendlyDuration(elapsedTime));
        miniPlayerFragment.updateProgress(player);
        seekBarHandler.removeCallbacks(updateProgressRunnable);

        int playbackState = player.getPlaybackState();
        if (!(playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED)) {
            seekBarHandler.postDelayed(updateProgressRunnable, 500);
        }
    }

    private Runnable updateProgressRunnable = () -> updateProgress();

    // Code below is for player swipe gesture

    /**
     * Converts dp to px
     *
     * @param dp
     * @return equivalent in px
     */
    private int convertToPixels(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    public void setUpSwipeAction() {
        // Initialise views involved
        View view = getView();
        ConstraintLayout miniPlayerLayout = view.findViewById(R.id.mini_player);
        FragmentContainerView miniPlayerView = view.findViewById(R.id.mini_player_fragment_view);
        ConstraintLayout maximimisedPlayer = view.findViewById(R.id.maximised_player_view);
        BottomNavigationView bottomNav = ((MainActivity) getActivity()).getBottomNav();

        // Set up constants
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        float miniPlayerHeight = convertToPixels(64); // miniPlayerView.getMeasuredHeight();
        float bottomNavHeight = convertToPixels(48); // bottomNav.getMeasuredHeight();

        float startY = screenHeight - bottomNavHeight - miniPlayerHeight;
        float targetY = convertToPixels(0);

        // Initial setup
        miniPlayerLayout.setY(startY);

        // Set up swipe action
        swipeAction = new PlayerSwipeAction(new GestureDetector(getContext(), new SingleTapGesture()));
        swipeAction.setDirection(SwipeAction.DragDirection.Up);
        swipeAction.setSteps(new float[]{startY, targetY});
        swipeAction.setDragThreshold(0.2f);
        swipeAction.setSwipeActionListener(new SwipeActionListener() {
            @Override
            public void onDragStart(float y, float friction) { }

            @Override
            public void onDrag(float y, float friction) {
                // Make mini player change in y
                miniPlayerLayout.setY(y);

                // Progress Gauge
                float gauge = y / startY; // expanded -> 0.0, collapsed -> 1.0
                float inverseGauge = 1 - gauge; // expanded -> 1.0, collapsed -> 0.0

                // Invisible when expanded
                miniPlayerView.setAlpha(gauge);

                bottomNav.setAlpha(gauge);
                bottomNav.setY(screenHeight - (bottomNavHeight * gauge));

                // Visible when expanded
                maximimisedPlayer.setAlpha(inverseGauge);
//                Log.d("DRAG_END_",  " " + v + " " + alpha);
            }

            @Override
            public void onDragEnd(float v, float v1) {
                if (v == 0.0) {
                    // expanded
                    maximimisedPlayer.setZ(10);
                    miniPlayerView.setZ(5);
                } else {
                    maximimisedPlayer.setZ(5);
                    miniPlayerView.setZ(10);
                }
            }
        });

        // Set up swipe touch listener
        OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener();
        swipeTouchListener.addAction(swipeAction);
        swipeTouchListener.attachToView(miniPlayerView);



        // Ensure mini player is collapsed
        swipeAction.collapse();
    }

    private class PlayerSwipeAction extends SwipeAction {
        private GestureDetector gestureDetector;

        public PlayerSwipeAction(GestureDetector gestureDetector) {
            super();
            this.gestureDetector = gestureDetector;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event) && !isExtended()) {
                expand();
            }
            return super.onTouch(v, event);
        }
    }

    private class SingleTapGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
