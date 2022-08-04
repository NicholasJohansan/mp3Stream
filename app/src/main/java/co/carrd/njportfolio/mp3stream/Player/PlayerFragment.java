package co.carrd.njportfolio.mp3stream.Player;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.colorgreen.swiper.OnSwipeTouchListener;
import com.colorgreen.swiper.SwipeAction;
import com.colorgreen.swiper.SwipeActionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;

public class PlayerFragment extends Fragment {

    private MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();
    private ImageButton minimizeButton;
    private SwipeAction swipeAction;

    private static PlayerFragment instance;
    private PlayerViewModel playerViewModel;

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

        return fragmentView;
    }

    /**
     * Converts dp to px
     *
     * @param dp
     * @return equivalent in px
     */
    private int convertToPixels(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inflate Mini Player Fragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.mini_player_fragment_view, miniPlayerFragment)
                .commit();

        // Set Up Swipe Action
        setUpSwipeAction();

        // Set Up Minimize Button
        minimizeButton.setOnClickListener(v -> swipeAction.collapse());

        // Instantiate PlayerViewModel
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);

        // Set up observer
        playerViewModel.getCurrentSong().observeForever(song -> {
            // TODO: bind song
        });
    }

    public void setSong(Song song) {
        playerViewModel.getCurrentSong().setValue(song);
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
        swipeAction = new SwipeAction();
        swipeAction.setDirection(SwipeAction.DragDirection.Up);
        swipeAction.setSteps(new float[]{startY, targetY});
        swipeAction.setDragThreshold(0.1f);
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
            }
        });

        // Set up swipe touch listener
        OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener();
        swipeTouchListener.addAction(swipeAction);
        swipeTouchListener.attachToView(miniPlayerView);

        // Ensure mini player is collapsed
        swipeAction.collapse();
    }
}
