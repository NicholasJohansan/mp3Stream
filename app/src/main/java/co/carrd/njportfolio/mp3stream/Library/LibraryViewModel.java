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

    public void updateData() {
        Hawk.put("likedSongsId", likedSongsIdList.getValue());
    }

    public MutableLiveData<List<Integer>> getLikedSongsIdList() {
        return likedSongsIdList;
    }

    public boolean songIsLiked(int songId) {
        return likedSongsIdList.getValue().contains(songId);
    }

    public void toggleLike(int songId) {
        List<Integer> likedSongsIdList = getLikedSongsIdList().getValue();
        if (songIsLiked(songId)) {
            likedSongsIdList.remove((Integer) songId);
        } else {
            likedSongsIdList.add(songId);
        }
        getLikedSongsIdList().setValue(likedSongsIdList);
        updateData();
    }
}
