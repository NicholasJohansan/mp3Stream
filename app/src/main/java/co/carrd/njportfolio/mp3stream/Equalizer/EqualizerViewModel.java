package co.carrd.njportfolio.mp3stream.Equalizer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EqualizerViewModel extends ViewModel {
    private MutableLiveData<int[]> bandLevels = new MutableLiveData<>(new int[5]);

    public MutableLiveData<int[]> getBandLevels() {
        return bandLevels;
    }
}
