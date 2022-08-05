package co.carrd.njportfolio.mp3stream.Equalizer;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.bindBandData((short) position);
    }

    public class EqualizerBandViewHolder extends RecyclerView.ViewHolder {
        private SeekBar equalizerBandBar;
        private EqualizerViewModel equalizerViewModel;

        public EqualizerBandViewHolder(@NonNull View itemView) {
            super(itemView);
            equalizerBandBar = itemView.findViewById(R.id.equalizer_band_bar);
            equalizerViewModel = new ViewModelProvider(activity).get(EqualizerViewModel.class);
        }

        public void bindBandData(short bandNumber) {

            // Get initial values
            short bandLevel = equalizer.getBandLevel((short) bandNumber);
            short[] bandLevelRange = equalizer.getBandLevelRange();

            // Set initial values on equalizer band
            equalizerBandBar.setMin(bandLevelRange[0]);
            equalizerBandBar.setMax(bandLevelRange[1]);
            equalizerBandBar.setProgress(bandLevel);

            // Set equalizer band to observe value of its band level
            equalizerViewModel.getBandLevels().observeForever(bandLevels -> {
                equalizerBandBar.setProgress(bandLevels[bandNumber]);
            });

            // Configure equalizer band to update equalizer band level when value is changed
            equalizerBandBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                private boolean touchStarted;
                private int progress;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (touchStarted && fromUser) {
                        this.progress = progress;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    touchStarted = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    touchStarted = false;
                    equalizerViewModel.updateBandLevel((short) progress, bandNumber, equalizer);
                }
            });
        }
    }
}
