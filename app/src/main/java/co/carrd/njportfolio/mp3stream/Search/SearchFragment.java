package co.carrd.njportfolio.mp3stream.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        setUpStyles(fragmentView);
        return fragmentView;
    }

    private void setUpStyles(View fragmentView) {
        UiUtils.setToolbarGradientTitle(fragmentView);
    }
}
