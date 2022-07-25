package co.carrd.njportfolio.mp3stream.Search.Classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.R;

public class SearchResultsFragment extends Fragment {
    private String label;
    private TextView labelTextView;

    public SearchResultsFragment(String label) {
        super();
        this.label = label;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.fragment_search_results, container, false);
        labelTextView = fragmentView.findViewById(R.id.search_results_fragment_label);
        labelTextView.setText(label);
        return fragmentView;
    }
}
