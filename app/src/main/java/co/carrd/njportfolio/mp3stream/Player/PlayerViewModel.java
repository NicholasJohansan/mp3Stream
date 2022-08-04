package co.carrd.njportfolio.mp3stream.Player;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<List<Song>> playerQueue = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Song> currentSong = new MutableLiveData<>(null);

    public MutableLiveData<Song> getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song song) {
        currentSong.setValue(song);
    }
}
