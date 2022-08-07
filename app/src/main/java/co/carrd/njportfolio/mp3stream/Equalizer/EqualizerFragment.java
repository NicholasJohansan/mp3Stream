package co.carrd.njportfolio.mp3stream.Equalizer;

import android.app.Dialog;
import android.graphics.Rect;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.materialswitch.MaterialSwitch;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class EqualizerFragment extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private Equalizer equalizer;
    private EqualizerViewModel equalizerViewModel;

    private AutoCompleteTextView presetDropdown;
    private MaterialSwitch enableSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_equalizer, container, false);
        UiUtils.setToolbarGradientTitle(fragmentView);

        // Link UI
        recyclerView = fragmentView.findViewById(R.id.equalizer_bands_recycler_view);
        enableSwitch = fragmentView.findViewById(R.id.equalizer_enabled_switch);
        presetDropdown = fragmentView.findViewById(R.id.preset_text_field);

        // Link View Model
        equalizerViewModel = new ViewModelProvider(requireActivity()).get(EqualizerViewModel.class);

        // Initialise Equalizer
        equalizer = MainApplication.getInstance().getEqualizer();

        return fragmentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.Theme_Mp3Stream_BottomSheetDialog);
        dialog.setOnShowListener(dialogInterface -> {
            ImageButton closeDialogButton = dialog.findViewById(R.id.equalizer_fragment_close_dialog_button);
            closeDialogButton.setVisibility(View.VISIBLE);
            closeDialogButton.setOnClickListener(v -> dismiss());
        });
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Sync equalizer to stored data
        equalizerViewModel.syncEqualizer(equalizer);

        // Fetch all presets
        String[] presets = new String[equalizer.getNumberOfPresets()];
        for (short i = 0; i < presets.length; i++) {
            presets[i] = equalizer.getPresetName(i);
        }
        // Configure preset dropdown menu with fetched presets
        presetDropdown.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, presets) {
            // Disable filtering for the autocompletetextview
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence) {
                        return null;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    }
                };
            }
        });

        // Set preset dropdown to observe selected preset index
        equalizerViewModel.getSelectedPresetIndex().observe(getViewLifecycleOwner(), selectedIndex -> {
            if (selectedIndex != -1) {
                // Valid preset was selected
                equalizerViewModel.usePreset(equalizer, (short) (int) selectedIndex);
                presetDropdown.setText(presets[selectedIndex], false);
            } else {
                // Custom (not a preset)
                presetDropdown.clearListSelection();
                presetDropdown.setText("Custom", false);
            }
        });

        // Configure preset to be set when selected from preset dropdown menu
        presetDropdown.setOnItemClickListener((adapterView, itemView, position, id) -> {
            equalizerViewModel.getSelectedPresetIndex().setValue(position);
        });

        // Set up equalizer band recycler view
        recyclerView.setAdapter(new EqualizerBandsAdapter(equalizer, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        // Add Item Decoration that is configured to give equal spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                // Set up dimensions
                int totalWidth = parent.getMeasuredWidth();
                int itemCount = state.getItemCount();
                int equalizerBandWidth = convertToPixels(32);

                // Calculate dimensions
                int totalSpacing = (totalWidth) - (equalizerBandWidth * itemCount);
                int spacing = totalSpacing / (itemCount + 1); // itemCount + 1 -> number of gaps

                // Set offset
                outRect.left = spacing;
            }
        });

        // Configure equalizer to update band levels if changed from outside applications
        equalizer.setParameterListener((equalizer, status, paramType, bandNum, value) -> {
            if (paramType == Equalizer.PARAM_BAND_LEVEL) {
                equalizerViewModel.updateBandLevel((short) value, (short) bandNum, equalizer);
            }
        });

        // Configure switch to reflect state based on view model
        equalizerViewModel.getEnabled().observe(getViewLifecycleOwner(), enabled -> {
            enableSwitch.setChecked(enabled);
        });

        // Configure enable switch to toggle whether equalizer is active
        enableSwitch.setOnClickListener(v -> equalizerViewModel.toggleEnabled(equalizer));
    }

    private int convertToPixels(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }
}
