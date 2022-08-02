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

public class PlaylistDetailsFragment extends Fragment {
    private Playlist playlist;

    private ImageButton navBackButton;
    private ImageView coverImageView;
    private TextView playlistNameTextView;
    private TextView songCountTextView;
    private TextView durationTextView;

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

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up back nav
        navBackButton.setOnClickListener(v -> SearchFragment.getInstance().popBackStack());

        // Load cover image
        loadImage(view, coverImageView, playlist.getCoverUrl());

        // Load playlist metadata
        playlistNameTextView.setText(playlist.getTitle());
        songCountTextView.setText(playlist.getSongCount() + " Songs");
        durationTextView.setText(playlist.getFriendlyDuration());

    }

    private void loadImage(View itemView, ImageView imageView, String imageUrl) {

        // Create placeholder progressbar while loading image
        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(itemView.getContext());
        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.gray, null));
        progressDrawable.setColorFilter(ResourcesCompat.getColor(itemView.getResources(), R.color.orange, null), PorterDuff.Mode.SCREEN);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(40f);
        progressDrawable.start();

        // Load with glide
        imageUrl = imageUrl == null ? "" : imageUrl;
        Glide.with(itemView)
                .load(imageUrl)
                .error(R.drawable.ic_launcher_foreground)
                .placeholder(progressDrawable)
                .override(Target.SIZE_ORIGINAL)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.light_gray, null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), R.color.light_gray, null));
                        return false;
                    }
                })
                .into(imageView);
    }
}
