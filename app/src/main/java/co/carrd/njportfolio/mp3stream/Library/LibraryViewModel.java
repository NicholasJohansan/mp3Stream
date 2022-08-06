package co.carrd.njportfolio.mp3stream.Library;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewModel extends ViewModel {
    private MutableLiveData<List<Integer>> likedSongsIdList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Integer>> savedSongsIdList = new MutableLiveData<>(new ArrayList<>());

    public void syncData() {
        likedSongsIdList.setValue(Hawk.get("likedSongsId", new ArrayList<>()));
        savedSongsIdList.setValue(Hawk.get("savedSongsId", new ArrayList<>()));
    }

    public void updateData() {
        Hawk.put("likedSongsId", likedSongsIdList.getValue());
        Hawk.put("savedSongsId", savedSongsIdList.getValue());
    }

    // Liked Songs Id List Code

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

    // Saved Songs Id List Code

    public MutableLiveData<List<Integer>> getSavedSongsIdList() {
        return savedSongsIdList;
    }

    public boolean getSongIsSaved(int songId) {
        return savedSongsIdList.getValue().contains(songId);
    }

    public void toggleSave(int songId) {
        List<Integer> savedSongsIdList = getSavedSongsIdList().getValue();
        if (songIsLiked(songId)) {
            savedSongsIdList.remove((Integer) songId);
        } else {
            savedSongsIdList.add(songId);
        }
        getSavedSongsIdList().setValue(savedSongsIdList);
        updateData();
    }
}
