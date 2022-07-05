package co.carrd.njportfolio.mp3stream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import co.carrd.njportfolio.mp3stream.Fragments.EqualizerFragment;
import co.carrd.njportfolio.mp3stream.Fragments.LibraryFragment;
import co.carrd.njportfolio.mp3stream.Fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, new LibraryFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.bottom_nav_library:
                selectedFragment = new LibraryFragment();
                break;
            case R.id.bottom_nav_search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.bottom_nav_equalizer:
                selectedFragment = new EqualizerFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, selectedFragment).commit();

        return true;
    };
}