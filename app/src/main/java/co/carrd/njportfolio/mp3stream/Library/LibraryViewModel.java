package co.carrd.njportfolio.mp3stream.Library;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewModel extends ViewModel {
    private MutableLiveData<List<Integer>> likedSongsIdList = new MutableLiveData<>(new ArrayList<>());

    public void syncData() {
        likedSongsIdList.setValue(Hawk.get("likedSongsId", new ArrayList<>()));
    }

    public MutableLiveData<List<Integer>> getLikedSongsIdList() {
        return likedSongsIdList;
    }
}
