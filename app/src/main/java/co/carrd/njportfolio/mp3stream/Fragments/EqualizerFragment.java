package co.carrd.njportfolio.mp3stream.Fragments;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class EqualizerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_equalizer, container, false);
        setUpStyles(fragmentView);

        AutoCompleteTextView presetTextView = fragmentView.findViewById(R.id.preset_text_field);
        Equalizer equalizer = new Equalizer(0, new MediaPlayer().getAudioSessionId());
        String[] presets = new String[equalizer.getNumberOfPresets()];
        for (short i = 0; i < presets.length; i++) {
            presets[i] = equalizer.getPresetName(i);
        }
        presetTextView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, presets));
        presetTextView.setText(presets[0], false);

        return fragmentView;
    }

    private void setUpStyles(View fragmentView) {
        UiUtils.setToolbarGradientTitle(fragmentView);
    }
}
