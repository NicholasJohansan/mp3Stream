package co.carrd.njportfolio.mp3stream.Search.ViewHolders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Details.ArtistDetailsFragment;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Artist;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class ArtistResultViewHolder extends RecyclerView.ViewHolder {
    private TextView artistNameTextView;
    private TextView songCountTextView;
    private TextView playlistCountTextView;
    private ImageView avatarImageView;

    public ArtistResultViewHolder(@NonNull View itemView) {
        super(itemView);
        artistNameTextView = itemView.findViewById(R.id.artist_result_item_name);
        songCountTextView = itemView.findViewById(R.id.artist_result_item_song_count);
        playlistCountTextView = itemView.findViewById(R.id.artist_result_item_playlist_count);
        avatarImageView = itemView.findViewById(R.id.artist_result_item_avatar);
    }

    public void bindArtist(Artist artist) {

        artistNameTextView.setText(artist.getName());
        songCountTextView.setText(artist.getSongCount() + " Songs");
        playlistCountTextView.setText(artist.getPlaylistCount() + " Playlists");

        UiUtils.loadImage(avatarImageView, artist.getAvatarUrl());

        itemView.setOnClickListener(view -> {
            Fragment artistDetailsFragment = new ArtistDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("artist", artist);
            artistDetailsFragment.setArguments(args);
            SearchFragment.getInstance().addToBackStack(artistDetailsFragment);
        });

    }
}