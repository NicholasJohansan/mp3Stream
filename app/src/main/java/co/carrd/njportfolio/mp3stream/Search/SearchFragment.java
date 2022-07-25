package co.carrd.njportfolio.mp3stream.Search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;

import co.carrd.njportfolio.mp3stream.MainActivity;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Classes.SearchResultsFragment;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class SearchFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AutoCompleteTextView searchEditText;

    private SearchResultsFragment[] searchResultsFragments = new SearchResultsFragment[] {
            new SearchResultsFragment("Songs"),
            new SearchResultsFragment("Playlists"),
            new SearchResultsFragment("Albums"),
            new SearchResultsFragment("Artists")
    };

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
        viewPager.setAdapter(new SearchResultsFragmentAdapter(this.requireActivity()));
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                new String[] {
                        "ok"
                });
        searchEditText.setAdapter(arrayAdapter);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchEditText.dismissDropDown();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                        new String[] {});
                searchEditText.setAdapter(arrayAdapter);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("lel", charSequence.toString());
                String newText = charSequence.toString();
                // TODO: Set To New List
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                        new String[] {
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchEditText.showDropDown();
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
}
