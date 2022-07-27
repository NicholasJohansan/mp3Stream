package co.carrd.njportfolio.mp3stream.Search.Classes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.Models.SongCollection;

public class SearchResultsFragment extends Fragment {
    private String label;
    private TextView labelTextView;
    private SearchFragment searchFragment;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchResultsAdapter searchResultsAdapter;

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
        recyclerView = fragmentView.findViewById(R.id.search_results_recycler_view);
        progressBar = fragmentView.findViewById(R.id.search_endless_progress);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up recycler view
        searchResultsAdapter = new SearchResultsAdapter();
        recyclerView.setAdapter(searchResultsAdapter);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (searchResultsAdapter.getNextUrl() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    MainApplication.getInstance().getSoundcloudApi()
                            .getNextTracks(searchResultsAdapter.getNextUrl(), songCol -> {
                                getActivity().runOnUiThread(() -> {
                                    List<? extends Object> castedResults = songCol.getSongs();
                                    addNewSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                                });
                            });
                }
            }
        });

        // Ensure that search is also performed if the fragment has not been initialised
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

            if (searchQuery.equals("")) return;
            progressBar.setVisibility(View.VISIBLE);
            MainApplication.getInstance().getSoundcloudApi()
                    .searchTracks(searchQuery, songCol -> {
                        getActivity().runOnUiThread(() -> {
                            List<? extends Object> castedResults = songCol.getSongs();
                            setSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                        });
                    });

            noResultsView.setVisibility(View.GONE);
            notSearchedView.setVisibility(View.GONE);
            labelTextView.setText(label + " Search: " + searchQuery);
        }
    }

    public void setSearchResults(List<Object> searchResults, String nextUrl) {
        searchResultsAdapter.getSearchResults().setValue(searchResults);
        searchResultsAdapter.setNextUrl(nextUrl);
        searchResultsAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void addNewSearchResults(List<Object> newSearchResults, String nextUrl) {
        int originalResultsLength = searchResultsAdapter.getSearchResults().getValue().size();
        searchResultsAdapter.addSearchResults(newSearchResults);
        searchResultsAdapter.setNextUrl(nextUrl);
        if (searchResultsAdapter.getNextUrl() == null) {
            progressBar.setVisibility(View.GONE);
        }
        searchResultsAdapter.notifyItemRangeInserted(originalResultsLength, newSearchResults.size());
        progressBar.setVisibility(View.INVISIBLE);
    }
}
