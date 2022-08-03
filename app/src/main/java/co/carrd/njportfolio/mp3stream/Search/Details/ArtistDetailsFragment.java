package co.carrd.njportfolio.mp3stream.Search.Details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Artist;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class ArtistDetailsFragment extends Fragment {
    private Artist artist;

    private ImageButton navBackButton;
    private ImageView bannerImageView;
    private ImageView avatarImageView;
    private TextView artistNameTextView;
    private TextView songCountTextView;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private ArtistWorksFragment[] artistWorksFragments = new ArtistWorksFragment[] {
            new ArtistWorksFragment(ArtistWorksFragment.WorkType.SONG),
            new ArtistWorksFragment(ArtistWorksFragment.WorkType.PLAYLIST),
            new ArtistWorksFragment(ArtistWorksFragment.WorkType.ALBUM)
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_artist_details, container, false);

        // Retrieve data
        Bundle args = getArguments();
        artist = args.getParcelable("artist");

        // Link UI
        navBackButton = fragmentView.findViewById(R.id.nav_back_button);
        bannerImageView = fragmentView.findViewById(R.id.artist_details_banner);
        avatarImageView = fragmentView.findViewById(R.id.artist_details_avatar);
        artistNameTextView = fragmentView.findViewById(R.id.artist_details_name_text_view);
        songCountTextView = fragmentView.findViewById(R.id.artist_details_song_count_text_view);
        viewPager = fragmentView.findViewById(R.id.artist_details_view_pager);
        tabLayout = fragmentView.findViewById(R.id.artist_details_tab_layout);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load images
        UiUtils.loadImage(bannerImageView, artist.getBannerUrl(), R.drawable.ic_launcher_background);
        UiUtils.loadImage(avatarImageView, artist.getAvatarUrl());

        // Load artist metadata
        artistNameTextView.setText(artist.getName());
        songCountTextView.setText(artist.getSongCount() + " Songs");

        // Set up nav back button
        navBackButton.setOnClickListener(v -> SearchFragment.getInstance().popBackStack());

        // Set up ViewPager2 + TabLayout
        viewPager.setAdapter(new ArtistWorksFragmentAdapter(requireActivity()));
        viewPager.setOffscreenPageLimit(4);
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
            }
            tab.setText(text);
        }).attach();
    }

    private class ArtistWorksFragmentAdapter extends FragmentStateAdapter {

        public ArtistWorksFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return artistWorksFragments[position];
        }

        @Override
        public int getItemCount() {
            return artistWorksFragments.length;
        }
    }
}
