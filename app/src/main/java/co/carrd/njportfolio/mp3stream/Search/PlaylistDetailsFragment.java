package co.carrd.njportfolio.mp3stream.Search;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class PlaylistDetailsFragment extends Fragment {
    private Playlist playlist;

    private ImageButton navBackButton;
    private ImageView coverImageView;
    private TextView playlistNameTextView;
    private TextView songCountTextView;
    private TextView durationTextView;
    private TextView topbarTitleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_playlist_details, container, false);

        // Retrieve data
        Bundle args = getArguments();
        playlist = args.getParcelable("playlist");

        // Link UI
        navBackButton = fragmentView.findViewById(R.id.nav_back_button);
        coverImageView = fragmentView.findViewById(R.id.playlist_details_cover);
        playlistNameTextView = fragmentView.findViewById(R.id.playlist_details_name_text_view);
        songCountTextView = fragmentView.findViewById(R.id.playlist_details_song_count_text_view);
        durationTextView = fragmentView.findViewById(R.id.playlist_details_duration_text_view);
        topbarTitleTextView = fragmentView.findViewById(R.id.playlist_details_topbar_title);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up back nav
        navBackButton.setOnClickListener(v -> SearchFragment.getInstance().popBackStack());

        // Load cover image
        UiUtils.loadImage(coverImageView, playlist.getCoverUrl());

        // Load playlist metadata
        playlistNameTextView.setText(playlist.getTitle());
        songCountTextView.setText(playlist.getSongCount() + " Songs");
        durationTextView.setText(playlist.getFriendlyDuration());
        topbarTitleTextView.setText(playlist.isAlbum() ? "Album" : "Playlist");

    }
}
