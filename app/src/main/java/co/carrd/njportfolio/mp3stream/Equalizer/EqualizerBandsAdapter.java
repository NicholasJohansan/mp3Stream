package co.carrd.njportfolio.mp3stream.Equalizer;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import co.carrd.njportfolio.mp3stream.R;

public class EqualizerBandsAdapter extends RecyclerView.Adapter<EqualizerBandsAdapter.EqualizerBandViewHolder> {
    private Equalizer equalizer;
    private FragmentActivity activity;

    public EqualizerBandsAdapter(Equalizer equalizer, FragmentActivity activity) {
        this.equalizer = equalizer;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EqualizerBandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View equalizerBandView = inflater.inflate(R.layout.item_equalizer_band, parent, false);
        return new EqualizerBandViewHolder(equalizerBandView);
    }

    @Override
    public int getItemCount() {
        return equalizer.getNumberOfBands();
    }

    @Override
    public void onBindViewHolder(@NonNull EqualizerBandViewHolder holder, int position) {
        holder.bindBandData(position);
    }

    public class EqualizerBandViewHolder extends RecyclerView.ViewHolder {
        private SeekBar equalizerBandBar;
        private EqualizerViewModel equalizerViewModel;

        public EqualizerBandViewHolder(@NonNull View itemView) {
            super(itemView);
            equalizerBandBar = itemView.findViewById(R.id.equalizer_band_bar);
            equalizerViewModel = new ViewModelProvider(activity).get(EqualizerViewModel.class);
        }

        public void bindBandData(int bandNumber) {
            //
            short band = equalizer.getBand(bandNumber);
            short bandLevel = equalizer.getBandLevel((short) bandNumber);
            int centerFreq = equalizer.getCenterFreq((short) bandNumber);
            String freqRange = String.join(",", Arrays.stream(equalizer.getBandFreqRange((short) bandNumber)).mapToObj(String::valueOf).toArray(String[]::new));
            short[] bandLevelRange = equalizer.getBandLevelRange();
            String levelRange = "";
            for (short s : bandLevelRange) {
                levelRange += "," + s;
            }
            equalizerBandBar.setMin(bandLevelRange[0]);
            equalizerBandBar.setMax(bandLevelRange[1]);
            equalizerBandBar.setProgress(bandLevel);
            Log.d("EQUALIZER", band + " " + bandLevel + " " + centerFreq + " " + freqRange + " " + levelRange);
            equalizerViewModel.getBandLevels().observeForever(bandLevels -> {
                equalizerBandBar.setProgress(bandLevels[bandNumber]);
            });
        }
    }
}
