package co.carrd.njportfolio.mp3stream.Search.Details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Classes.EndlessRecyclerViewScrollListener;
import co.carrd.njportfolio.mp3stream.Search.Results.SearchResultsAdapter;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class ArtistWorksFragment extends Fragment {
    private WorkType workType;

    private TextView labelTextView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchResultsAdapter searchResultsAdapter;

    private View noWorksView;
    private TextView noWorksTextView;

    public enum WorkType {
        SONG("Songs"),
        PLAYLIST("Playlists"),
        ALBUM("Albums");

        public final String value;
        private WorkType(String value) {
            this.value = value;
        }
    }

    public ArtistWorksFragment(WorkType workType) {
        this.workType = workType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_artist_works, container, false);

        // Link UI
        noWorksTextView = fragmentView.findViewById(R.id.artist_works_no_works_text_view);
        noWorksView = fragmentView.findViewById(R.id.artist_works_no_works_view);
        recyclerView = fragmentView.findViewById(R.id.artist_works_recycler_view);
        progressBar = fragmentView.findViewById(R.id.artist_works_endless_progress);
        labelTextView = fragmentView.findViewById(R.id.artist_works_fragment_label);

        return fragmentView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up label
        labelTextView.setText(workType.value);
        noWorksTextView.setText("No " + workType.value.toLowerCase() + " found");

        // Set up recycler view
        searchResultsAdapter = new SearchResultsAdapter();
        recyclerView.setAdapter(searchResultsAdapter);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                if (searchResultsAdapter.getNextUrl() != null) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();
//                    switch (workType) {
//                        case SONG:
//                            soundcloudApi.getNextTracks(searchResultsAdapter.getNextUrl(), songCol -> {
//                                UiUtils.runOnUiThread(getActivity(), () -> {
//                                    List<? extends Object> castedResults = songCol.getSongs();
//                                    addNewSearchResults((List<Object>) castedResults, songCol.getNextUrl());
//                                });
//                            });
//                            break;
//                        case PLAYLIST:
//                            soundcloudApi.getNextPlaylists(searchResultsAdapter.getNextUrl(), playlistCol -> {
//                                UiUtils.runOnUiThread(getActivity(), () -> {
//                                    List<? extends Object> castedResults = playlistCol.getPlaylists();
//                                    addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
//                                });
//                            });
//                            break;
//                        case ALBUM:
//                            soundcloudApi.getNextAlbums(searchResultsAdapter.getNextUrl(), playlistCol -> {
//                                UiUtils.runOnUiThread(getActivity(), () -> {
//                                    List<? extends Object> castedResults = playlistCol.getPlaylists();
//                                    addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
//                                });
//                            });
//                            break;
//                    }
//                }
            }
        });

        getInitialData();
    }

    public void getInitialData() {

//        progressBar.setVisibility(View.VISIBLE);
//        ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();
//
//        switch (workType) {
//            case SONG:
//                soundcloudApi.searchTracks(searchQuery, songCol -> {
//                    UiUtils.runOnUiThread(getActivity(), () -> {
//                        List<? extends Object> castedResults = songCol.getSongs();
//                        setSearchResults((List<Object>) castedResults, songCol.getNextUrl());
//                    });
//                });
//                break;
//            case PLAYLIST:
//                soundcloudApi.searchPlaylists(searchQuery, playlistCol -> {
//                    UiUtils.runOnUiThread(getActivity(), () -> {
//                        List<? extends Object> castedResults = playlistCol.getPlaylists();
//                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
//                    });
//                });
//                break;
//            case ALBUM:
//                soundcloudApi.searchAlbums(searchQuery, playlistCol -> {
//                    UiUtils.runOnUiThread(getActivity(), () -> {
//                        List<? extends Object> castedResults = playlistCol.getPlaylists();
//                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
//                    });
//                });
//                break;
//        }
    }

    public void setSearchResults(List<Object> searchResults, String nextUrl) {

        if (searchResults.size() == 0) {
            noWorksView.setVisibility(View.VISIBLE);
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
