package co.carrd.njportfolio.mp3stream.Search.ViewHolders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Details.PlaylistDetailsFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class PlaylistResultViewHolder extends RecyclerView.ViewHolder {
    private TextView playlistNameTextView;
    private TextView artistTextView;
    private TextView songsDurationTextView;
    private ImageView coverImageView;

    public PlaylistResultViewHolder(@NonNull View itemView) {
        super(itemView);
        playlistNameTextView = itemView.findViewById(R.id.playlist_result_item_name);
        artistTextView = itemView.findViewById(R.id.playlist_result_item_artist);
        songsDurationTextView = itemView.findViewById(R.id.playlist_result_item_songs_duration);
        coverImageView = itemView.findViewById(R.id.playlist_result_item_cover);
    }

    public void bindPlaylist(Playlist playlist) {

        playlistNameTextView.setText(playlist.getTitle());
        artistTextView.setText(playlist.getArtist().getName());
        songsDurationTextView.setText(playlist.getSongCount() + " Songs ⋅ " + playlist.getFriendlyDuration());

        UiUtils.loadImage(coverImageView, playlist.getCoverUrl());

        itemView.setOnClickListener(view -> {
            Fragment playlistDetailsFragment = new PlaylistDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("playlist", playlist);
            playlistDetailsFragment.setArguments(args);
            SearchFragment.getInstance().addToBackStack(playlistDetailsFragment);
        });

    }
}