package co.carrd.njportfolio.mp3stream.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Playlist;

public class PlaylistDetailsFragment extends Fragment {
    private Playlist playlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_playlist_details, container, false);
        return fragmentView;
    }
}
