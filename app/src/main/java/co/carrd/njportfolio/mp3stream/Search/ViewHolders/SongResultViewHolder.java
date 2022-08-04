package co.carrd.njportfolio.mp3stream.Search.ViewHolders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.Player.PlayerFragment;
import co.carrd.njportfolio.mp3stream.Player.PlayerViewModel;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Details.TestFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SongResultViewHolder extends RecyclerView.ViewHolder {
    private TextView songNameTextView;
    private TextView artistTextView;
    private TextView durationTextView;
    private ImageView coverImageView;
    private ConstraintLayout georestrictedView;
    private ImageButton playButton;

    public SongResultViewHolder(@NonNull View itemView) {
        super(itemView);
        songNameTextView = itemView.findViewById(R.id.song_result_item_name);
        artistTextView = itemView.findViewById(R.id.song_result_item_artist);
        durationTextView = itemView.findViewById(R.id.song_result_item_duration);
        coverImageView = itemView.findViewById(R.id.song_result_item_cover);
        georestrictedView = itemView.findViewById(R.id.song_result_georestricted_view);
        playButton = itemView.findViewById(R.id.song_result_play_button);
    }

    public void bindSong(Song song) {

        songNameTextView.setText(song.getTitle());
        artistTextView.setText(song.getArtist().getName());
        durationTextView.setText(song.getFriendlyDuration());

        if (song.getPartialStreamUrl() == null) {
            georestrictedView.setVisibility(View.VISIBLE);
        } else {
            georestrictedView.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(view -> {
            Fragment newFragment = new TestFragment();
            Bundle args = new Bundle();
            args.putInt("num", 1);
            newFragment.setArguments(args);
            SearchFragment.getInstance().addToBackStack(newFragment);
        });

        playButton.setOnClickListener(view -> {
            PlayerFragment.getInstance().setSong(song);
        });

        UiUtils.loadImage(coverImageView, song.getCoverUrl());
    }
}