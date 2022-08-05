package co.carrd.njportfolio.mp3stream.Equalizer;

import android.media.audiofx.Equalizer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EqualizerViewModel extends ViewModel {
    private MutableLiveData<int[]> bandLevels = new MutableLiveData<>(new int[5]);

    public MutableLiveData<int[]> getBandLevels() {
        return bandLevels;
    }

    public void syncBandLevels(Equalizer equalizer) {
        int[] syncedBandLevels = new int[equalizer.getNumberOfBands()];
        for (int i = 0; i < equalizer.getNumberOfBands(); i++) {
            syncedBandLevels[i] = equalizer.getBandLevel((short) i);
        }
        bandLevels.setValue(syncedBandLevels);
    }

    public void updateBandLevel(short newBandLevel, short bandNum, Equalizer equalizer) {
        int[] modifiedBandLevels = bandLevels.getValue();
        modifiedBandLevels[bandNum] = newBandLevel;
        equalizer.setBandLevel(bandNum, newBandLevel);
        bandLevels.setValue(modifiedBandLevels);
    }
}
