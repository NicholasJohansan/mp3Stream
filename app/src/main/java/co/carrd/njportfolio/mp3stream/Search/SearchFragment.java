package co.carrd.njportfolio.mp3stream.Search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Results.SearchResultsFragment;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchFragment extends Fragment {

    private static SearchFragment instance;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AutoCompleteTextView searchEditText;

    public String searchQuery = null;
    private String latestSearchQuery = null;
    private boolean searchOpened = false;

    private SearchResultsFragment[] searchResultsFragments = new SearchResultsFragment[] {
            new SearchResultsFragment("Songs", this),
            new SearchResultsFragment("Playlists", this),
            new SearchResultsFragment("Albums", this),
            new SearchResultsFragment("Artists", this)
    };

    public static SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);

        // Link Variables
        viewPager = fragmentView.findViewById(R.id.search_view_pager);
        tabLayout = fragmentView.findViewById(R.id.search_tab_layout);
        searchEditText = fragmentView.findViewById(R.id.search_text_field);

        // Set Up Styles
        UiUtils.setToolbarGradientTitle(fragmentView);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set Up ViewPager2 + TabLayout
        viewPager.setAdapter(new SearchResultsFragmentAdapter(requireActivity()));
        viewPager.setOffscreenPageLimit(4);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String text = "";
            switch (position) {
                case 0:
                    text = "Songs";
                    break;
                case 1:
                    text = "Playlists";
                    break;
                case 2:
                    text = "Albums";
                    break;
                case 3:
                    text = "Artists";
                    break;
            }
            tab.setText(text);
        }).attach();

        // Set Up Search Edit Text
        searchEditText.setText(searchQuery, false);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();

                if (searchQuery != null && searchQuery.equals(newText) && !searchOpened) return;

                latestSearchQuery = newText;
                searchOpened = true;

                if (!newText.equals("")) {
                    MainApplication.getInstance().getSoundcloudApi()
                            .getSearchSuggestions(newText, data -> {
                                UiUtils.runOnUiThread(getActivity(), () -> {
                                    if (latestSearchQuery.equals(newText) && searchOpened) {
                                        searchEditText.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data));
                                        searchEditText.showDropDown();
                                    }
                                });
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                // Clear Suggestions
                searchEditText.dismissDropDown();
                searchOpened = false;
                searchEditText.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new String[] {}));

                // Stop Input
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                textView.clearFocus();

                // Submit Input
                String input = textView.getText().toString();
                searchQuery = input;
                for (SearchResultsFragment fragment : searchResultsFragments) {
                    fragment.submitSearch(input);
                }

                return true;
            }
            return false;
        });

        // Set Up Fragment Manager
        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
//
//
//                Log.d("HEJ", "back stack changed original: " + backStack.getValue().size() + " new: " + getParentFragmentManager().getBackStackEntryCount());
//                if (getParentFragmentManager().getBackStackEntryCount() == backStack.getValue().size() && !backStackBeingCleared) {
//                    // Back Stack was popped
//
//                    Log.d("HEJ", "remove");
//                    backStack.getValue().remove(backStack.getValue().size()-1);
//                    Log.d("HEJ", "removed size " + backStack.getValue().size());
//                }
            }
        });
    }

    private class SearchResultsFragmentAdapter extends FragmentStateAdapter {

        public SearchResultsFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return searchResultsFragments[position];
        }

        @Override
        public int getItemCount() {
            return searchResultsFragments.length;
        }
    }

    public void addToBackStack(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        String backStackTag = "SEARCH_FRAGMENT";
        if (fm.getBackStackEntryCount() > 1) {
            backStackTag = null;
        }
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(backStackTag)
                .commit();
        getParentFragmentManager().executePendingTransactions();
    }

    public void popBackStack() {
        getActivity().onBackPressed();
    }
}
