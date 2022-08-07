package co.carrd.njportfolio.mp3stream.Library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class LibraryFragment extends Fragment {
    private static LibraryFragment instance;
    private LibraryViewModel libraryViewModel;

    private RecyclerView libraryPlaylistsRecyclerView;
    private LibraryPlaylistsAdapter libraryPlaylistsAdapter;

    private TextView addSongsButton;
    private ConstraintLayout noSongsIndicator;

    public static LibraryFragment getInstance() {
        if (instance == null) {
            instance = new LibraryFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_library, container, false);
        setUpStyles(fragmentView);

        // Link UI
        libraryPlaylistsRecyclerView = fragmentView.findViewById(R.id.library_fragment_playlists_recycler_view);
        noSongsIndicator = fragmentView.findViewById(R.id.library_fragment_no_songs_indicator);
        addSongsButton = fragmentView.findViewById(R.id.library_fragment_song_add);

        libraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Fetch data
        libraryViewModel.syncData();

        // Set up recycler view
        libraryPlaylistsAdapter = new LibraryPlaylistsAdapter(this);
        libraryPlaylistsRecyclerView.setAdapter(libraryPlaylistsAdapter);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        libraryPlaylistsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        // Observe saved songs
        libraryViewModel.getSavedSongsIdList().observe(requireActivity(), savedSongsIdList -> {
            if (savedSongsIdList.size() == 0) {
                noSongsIndicator.setVisibility(View.VISIBLE);
            } else {
                noSongsIndicator.setVisibility(View.GONE);
            }
        });

        // Set up add song button
        addSongsButton.setOnClickListener(v -> {
            LibraryAddSongsFragment libraryAddSongsFragment = new LibraryAddSongsFragment();
            libraryAddSongsFragment.show(getChildFragmentManager(), libraryAddSongsFragment.getTag());
        });
    }

    public LibraryViewModel getLibraryViewModel() {
        return libraryViewModel;
    }

    private void setUpStyles(View fragmentView) {
        UiUtils.setToolbarGradientTitle(fragmentView);
    }

    public void addToBackStack(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        String backStackTag = "LIBRARY_FRAGMENT";
        if (fm.getBackStackEntryCount() > 1) {
            backStackTag = null;
        }
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(backStackTag)
                .commit();
        getParentFragmentManager().executePendingTransactions();
    }

    public void popBackStack() {
        getActivity().onBackPressed();
    }
}
