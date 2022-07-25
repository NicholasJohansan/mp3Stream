package co.carrd.njportfolio.mp3stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import co.carrd.njportfolio.mp3stream.Equalizer.EqualizerFragment;
import co.carrd.njportfolio.mp3stream.Library.LibraryFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment libraryFragment = new LibraryFragment();
    private Fragment searchFragment = new SearchFragment();
    private Fragment equalizerFragment = new EqualizerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null); // This line is needed to have gradient icons work

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, libraryFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

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
                .replace(R.id.main_fragment_container, selectedFragment).commit();

        return true;
    };
}