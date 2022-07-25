package co.carrd.njportfolio.mp3stream.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Classes.SearchResultsFragment;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

//    private SearchResultsFragment[] searchResultsFragments = new SearchResultsFragment[] {
//            new SearchResultsFragment("Songs"),
//            new SearchResultsFragment("Playlists"),
//            new SearchResultsFragment("Albums"),
//            new SearchResultsFragment("Artists")
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        // Link Variables
        viewPager = fragmentView.findViewById(R.id.search_view_pager);
        tabLayout = fragmentView.findViewById(R.id.search_tab_layout);

        // Set Up Styles
        UiUtils.setToolbarGradientTitle(fragmentView);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set Up ViewPager2 + TabLayout
        viewPager.setAdapter(new SearchResultsFragmentAdapter(this.requireActivity()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String text = "";
            switch (position) {
                case 0:
                    text = "Songs";
                    break;
                case 1:
                    text = "Playlists";
                    break;
                case 2:
                    text = "Albums";
                    break;
                case 3:
                    text = "Artists";
                    break;
            }
            tab.setText(text);
        }).attach();
    }

    private class SearchResultsFragmentAdapter extends FragmentStateAdapter {
        public SearchResultsFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        private SearchResultsFragment songSearchResultsFragment = new SearchResultsFragment("Songs");
        private SearchResultsFragment playlistSearchResultsFragment = new SearchResultsFragment("Playlists");
        private SearchResultsFragment albumSearchResultsFragment = new SearchResultsFragment("Albums");
        private SearchResultsFragment artistSearchResultsFragment = new SearchResultsFragment("Artists");


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = songSearchResultsFragment;
            switch (position) {
                case 0:
                    fragment = songSearchResultsFragment;
                    break;
                case 1:
                    fragment = playlistSearchResultsFragment;
                    break;
                case 2:
                    fragment = albumSearchResultsFragment;
                    break;
                case 3:
                    fragment = artistSearchResultsFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
