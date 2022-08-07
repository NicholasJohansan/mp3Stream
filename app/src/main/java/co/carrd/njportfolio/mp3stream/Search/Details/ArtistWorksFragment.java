package co.carrd.njportfolio.mp3stream.Search.Details;

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

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Classes.EndlessRecyclerViewScrollListener;
import co.carrd.njportfolio.mp3stream.Search.Results.SearchResultsAdapter;
import co.carrd.njportfolio.mp3stream.SoundcloudApi.ApiWrapper;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class ArtistWorksFragment extends Fragment {
    private WorkType workType;
    private ArtistDetailsFragment artistDetailsFragment;

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

    public ArtistWorksFragment(WorkType workType, ArtistDetailsFragment artistDetailsFragment) {
        this.workType = workType;
        this.artistDetailsFragment = artistDetailsFragment;
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
        searchResultsAdapter = new SearchResultsAdapter(this);
        recyclerView.setAdapter(searchResultsAdapter);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore();
            }
        });

        getInitialData();
    }

    public void getInitialData() {

        progressBar.setVisibility(View.VISIBLE);
        int artistId = artistDetailsFragment.getArtistId();
        ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();

        switch (workType) {
            case SONG:
                soundcloudApi.getArtistTracks(artistId, songCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = songCol.getSongs();
                        setSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                    });
                });
                break;
            case PLAYLIST:
                soundcloudApi.getArtistPlaylists(artistId, playlistCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = playlistCol.getPlaylists();
                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                    });
                });
                break;
            case ALBUM:
                soundcloudApi.getArtistAlbums(artistId, playlistCol -> {
                    UiUtils.runOnUiThread(getActivity(), () -> {
                        List<? extends Object> castedResults = playlistCol.getPlaylists();
                        setSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                    });
                });
                break;
        }
    }

    public void setSearchResults(List<Object> searchResults, String nextUrl) {

        if (searchResults.size() == 0 && nextUrl == "null") {
            noWorksView.setVisibility(View.VISIBLE);
            return;
        }

        searchResultsAdapter.getSearchResults().setValue(searchResults);
        searchResultsAdapter.setNextUrl(nextUrl);

        if (searchResults.size() == 0 && nextUrl != "null") {
            loadMore();
            return;
        }

        searchResultsAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void addNewSearchResults(List<Object> newSearchResults, String nextUrl) {
        int originalResultsLength = searchResultsAdapter.getSearchResults().getValue().size();
        searchResultsAdapter.addSearchResults(newSearchResults);
        searchResultsAdapter.setNextUrl(nextUrl);
        searchResultsAdapter.notifyItemRangeInserted(originalResultsLength, newSearchResults.size());

        if (newSearchResults.size() == 0 && nextUrl != "null") {
            loadMore();
            return;
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    public void loadMore() {
        if (searchResultsAdapter.getNextUrl() != null && !searchResultsAdapter.getNextUrl().equals("null")) {
            progressBar.setVisibility(View.VISIBLE);
            ApiWrapper soundcloudApi = MainApplication.getInstance().getSoundcloudApi();
            switch (workType) {
                case SONG:
                    soundcloudApi.getNextTracks(searchResultsAdapter.getNextUrl(), songCol -> {
                        UiUtils.runOnUiThread(getActivity(), () -> {
                            List<? extends Object> castedResults = songCol.getSongs();
                            addNewSearchResults((List<Object>) castedResults, songCol.getNextUrl());
                        });
                    });
                    break;
                case PLAYLIST:
                    soundcloudApi.getNextPlaylists(searchResultsAdapter.getNextUrl(), playlistCol -> {
                        UiUtils.runOnUiThread(getActivity(), () -> {
                            List<? extends Object> castedResults = playlistCol.getPlaylists();
                            addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                        });
                    });
                    break;
                case ALBUM:
                    soundcloudApi.getNextAlbums(searchResultsAdapter.getNextUrl(), playlistCol -> {
                        UiUtils.runOnUiThread(getActivity(), () -> {
                            List<? extends Object> castedResults = playlistCol.getPlaylists();
                            addNewSearchResults((List<Object>) castedResults, playlistCol.getNextUrl());
                        });
                    });
                    break;
            }
        }
    }
}
