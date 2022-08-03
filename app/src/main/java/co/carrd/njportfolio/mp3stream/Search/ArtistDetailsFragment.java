package co.carrd.njportfolio.mp3stream.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Artist;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class ArtistDetailsFragment extends Fragment {
    private Artist artist;

    private ImageView bannerImageView;
    private ImageView avatarImageView;
    private TextView artistNameTextView;
    private TextView songCountTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_artist_details, container, false);

        // Retrieve data
        Bundle args = getArguments();
        artist = args.getParcelable("artist");

        // Link UI
        bannerImageView = fragmentView.findViewById(R.id.artist_details_banner);
        avatarImageView = fragmentView.findViewById(R.id.artist_details_avatar);
        artistNameTextView = fragmentView.findViewById(R.id.artist_details_name_text_view);
        songCountTextView = fragmentView.findViewById(R.id.artist_details_song_count_text_view);

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
    }
}
