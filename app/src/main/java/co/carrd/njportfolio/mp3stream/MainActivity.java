package co.carrd.njportfolio.mp3stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.colorgreen.swiper.OnSwipeTouchListener;
import com.colorgreen.swiper.SwipeAction;
import com.colorgreen.swiper.SwipeActionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import co.carrd.njportfolio.mp3stream.Equalizer.EqualizerFragment;
import co.carrd.njportfolio.mp3stream.Library.LibraryFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment libraryFragment = new LibraryFragment();
    private Fragment searchFragment = SearchFragment.getInstance();
    private Fragment equalizerFragment = new EqualizerFragment();

    private LinearLayout miniPlayer;
    private RelativeLayout mainLayout;

    private int convertToPixels(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        float screenHeight = getResources().getDisplayMetrics().heightPixels;

        mainLayout = findViewById(R.id.main_layout);
        ConstraintLayout miniPlayerView = findViewById(R.id.mini_player_view);
        float targetHeight = getResources().getDisplayMetrics().heightPixels - convertToPixels(48);
        float startHeight = convertToPixels(0);
        miniPlayer = findViewById(R.id.mini_player);
        miniPlayer.setY(targetHeight);
        OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener();
        SwipeAction swipeAction = new SwipeAction();
        swipeAction.setDirection(SwipeAction.DragDirection.Up);
        swipeAction.setSteps(new float[]{targetHeight - convertToPixels(64), startHeight});
        swipeAction.setDragThreshold(0.2f);
        swipeAction.setSwipeActionListener(new SwipeActionListener() {
            @Override
            public void onDragStart(float v, float v1) {

            }

            @Override
            public void onDrag(float v, float v1) {
                miniPlayer.setY(v);
                bottomNav.setY(screenHeight - ((convertToPixels(48)) * (v/targetHeight)));
                Log.d("DRAG_END_",  " " + v + " " + (double) (v/targetHeight) + " " + 1792.0/1792.0 + " " + 1792/1792);
                bottomNav.setAlpha((float) (1.0 * (v/targetHeight)));
            }

            @Override
            public void onDragEnd(float v, float v1) {
                Log.d("DRAG_END", String.valueOf(v));
                // 0.0 -> expanded
                if (v == 0.0) {
                    bottomNav.setAlpha(0);
                    bottomNav.setY(screenHeight);
                } else {
                    bottomNav.setAlpha(1);
                    bottomNav.setY(screenHeight - convertToPixels(48));
                }
            }
        });
        swipeTouchListener.addAction(swipeAction);
        swipeTouchListener.attachToView(miniPlayerView);
        swipeAction.collapse();


        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null); // This line is needed to have gradient icons work

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_container, libraryFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

//        getSupportFragmentManager().saveBackStack("SEARCH_FRAGMENT");
//        getSupportFragmentManager().saveBackStack("LIBRARY_FRAGMENT");
//        getSupportFragmentManager().saveBackStack("EQUALIZER_FRAGMENT");
        getSupportFragmentManager().saveBackStack("SEARCH_FRAGMENT");

        switch (item.getItemId()) {
            case R.id.bottom_nav_library:
                selectedFragment = libraryFragment;
                break;
            case R.id.bottom_nav_search:
                selectedFragment = searchFragment;
                break;
            case R.id.bottom_nav_equalizer:
                selectedFragment = equalizerFragment;
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_container, selectedFragment).commit();

        getSupportFragmentManager().executePendingTransactions();

        if (selectedFragment.equals(searchFragment)) {
            try {
                getSupportFragmentManager().restoreBackStack("SEARCH_FRAGMENT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        getSupportFragmentManager().executePendingTransactions();

//        try {
//            getSupportFragmentManager().restoreBackStack(backStackName);
//        } catch (Exception e) {
//
//        }

        return true;
    };
}