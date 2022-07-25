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
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;

public class SearchResultsFragment extends Fragment {
    private String label;
    private TextView labelTextView;
    private SearchFragment searchFragment;

    private View notSearchedView;
    private View noResultsView;

    private boolean viewInitialised = false;

    public SearchResultsFragment(String label, SearchFragment searchFragment) {
        super();
        this.label = label;
        this.searchFragment = searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search_results, container, false);

        // Link Variables
        labelTextView = fragmentView.findViewById(R.id.search_results_fragment_label);
        notSearchedView = fragmentView.findViewById(R.id.view_not_searched);
        noResultsView = fragmentView.findViewById(R.id.view_no_results);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewInitialised = true;
        labelTextView.setText(label);
        submitSearch(searchFragment.searchQuery);
    }

    public void submitSearch(String searchQuery) {

        if (!viewInitialised) return;

        // Nothing was searched
        if (searchQuery == null || searchQuery.equals("")) {
            noResultsView.setVisibility(View.GONE);
            notSearchedView.setVisibility(View.VISIBLE);
        }

        // Something was searched
        else if (searchQuery != null) {
            // TODO: Handle Search
            noResultsView.setVisibility(View.GONE);
            notSearchedView.setVisibility(View.GONE);
            labelTextView.setText(label + " Search: " + searchQuery);
        }
    }
}
