package co.carrd.njportfolio.mp3stream.Library;

import android.app.Dialog;
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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import co.carrd.njportfolio.mp3stream.MainApplication;
import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.Results.SearchResultsFragment;
import co.carrd.njportfolio.mp3stream.Utils.UiUtils;

public class LibraryAddSongsFragment extends BottomSheetDialogFragment {
    private AutoCompleteTextView searchEditText;

    public String searchQuery = null;
    private String latestSearchQuery = null;
    private boolean searchOpened = false;

    private SearchResultsFragment searchResultsFragment = new SearchResultsFragment("Songs", this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_library_songs_add, container, false);

        // Link UI
        searchEditText = fragmentView.findViewById(R.id.search_text_field);

        return fragmentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext(), R.style.Theme_Mp3Stream_BottomSheetDialog);
        dialog.setOnShowListener(dialogInterface -> {
            ImageButton minimizeButton = dialog.findViewById(R.id.minimize_button);
            minimizeButton.setOnClickListener(v -> dismiss());
        });
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Inflate SearchResultsFragmentContainer
        getChildFragmentManager().beginTransaction()
                .replace(R.id.search_results_container_view, searchResultsFragment)
                .commit();

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
                searchResultsFragment.submitSearch(input);

                return true;
            }
            return false;
        });
    }
}
