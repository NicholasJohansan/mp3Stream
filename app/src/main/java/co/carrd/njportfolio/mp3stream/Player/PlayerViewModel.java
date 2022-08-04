package co.carrd.njportfolio.mp3stream.Player;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.Song;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<List<Song>> playerQueue = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Song> currentSong = null;

    public MutableLiveData<Song> getCurrentSong() {
        return currentSong;
    }
}
