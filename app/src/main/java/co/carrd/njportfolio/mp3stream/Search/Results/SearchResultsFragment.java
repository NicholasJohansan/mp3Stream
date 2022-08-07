package co.carrd.njportfolio.mp3stream.Search.Results;

import android.os.Bundle;
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

import co.carrd.njportfolio.mp3stream.Library.LibraryAddSongsFragment;
import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Classes.EndlessRecyclerViewScrollListener;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchResultsFragment extends Fragment {
    private String label;
    private TextView labelTextView;
    private Fragment searchFragment;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchResultsAdapter searchResultsAdapter;

    private View notSearchedView;
    private View noResultsView;

    private boolean viewInitialised = false;

    public SearchResultsFragment(String label, Fragment searchFragment) {
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
        searchResultsAdapter = new SearchResultsAdapter(this, label.equals("AddSongs"));
        recyclerView.setAdapter(searchResultsAdapter);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (searchResultsAdapter.getNextUrl() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();
                    if (label.equals("Songs") || label.equals("AddSongs")) {
                        soundcloudApi.getNextTracks(searchResultsAdapter.getNextUrl(), songCol -> {
                            UiUtils.runOnUiThread(getActivity(), () -> {
                                List<? extends Object> castedResults = songCol.getSongs();
                                addNewSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                            });
                        });
                    } else if (label.equals("Playlists")) {
                        soundcloudApi.getNextPlaylists(searchResultsAdapter.getNextUrl(), playlistCol -> {
                            UiUtils.runOnUiThread(getActivity(), () -> {
                                List<? extends Object> castedResults = playlistCol.getPlaylists();
                                addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                            });
                        });
                    } else if (label.equals("Albums")) {
                        soundcloudApi.getNextAlbums(searchResultsAdapter.getNextUrl(), playlistCol -> {
                            UiUtils.runOnUiThread(getActivity(), () -> {
                                List<? extends Object> castedResults = playlistCol.getPlaylists();
                                addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                            });
                        });
                    } else if (label.equals("Artists")) {
                        soundcloudApi.getNextArtists(searchResultsAdapter.getNextUrl(), artistCol -> {
                            UiUtils.runOnUiThread(getActivity(), () -> {
                                List<? extends Object> castedResults = artistCol.getArtists();
                                addNewSearchResults((List<Object>) castedResults, artistCol.getNextUrl());
                            });
                        });
                    }

                }
            }
        });

        // Ensure that search is also performed if the fragment has not been initialised
        viewInitialised = true;
        labelTextView.setText(label);
        if (searchFragment.getClass().getSimpleName().equals(SearchFragment.class.getSimpleName())) {
            submitSearch(((SearchFragment) searchFragment).searchQuery);
        } else {
            submitSearch(((LibraryAddSongsFragment) searchFragment).searchQuery);
        }
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
            recyclerView.smoothScrollToPosition(0);
            progressBar.setVisibility(View.VISIBLE);

            ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();
            if (label.equals("Songs") || label.equals("AddSongs")) {
                soundcloudApi.searchTracks(searchQuery, songCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = songCol.getSongs();
                        setSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                    });
                });
            } else if (label.equals("Playlists")) {
                soundcloudApi.searchPlaylists(searchQuery, playlistCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = playlistCol.getPlaylists();
                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                    });
                });
            } else if (label.equals("Albums")) {
                soundcloudApi.searchAlbums(searchQuery, playlistCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = playlistCol.getPlaylists();
                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                    });
                });
            } else if (label.equals("Artists")) {
                soundcloudApi.searchArtists(searchQuery, artistCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = artistCol.getArtists();
                        setSearchResults((List<Object>) castedResults, artistCol.getNextUrl());
                    });
                });
            }


            noResultsView.setVisibility(View.GONE);
            notSearchedView.setVisibility(View.GONE);
            labelTextView.setText(label + " Search: " + searchQuery);
        }
    }

    public void setSearchResults(List<Object> searchResults, String nextUrl) {

        if (searchResults.size() == 0) {
            notSearchedView.setVisibility(View.GONE);
            noResultsView.setVisibility(View.VISIBLE);
            return;
        }

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
