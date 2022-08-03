package co.carrd.njportfolio.mp3stream.Search.Details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.carrd.njportfolio.mp3stream.R;
import co.carrd.njportfolio.mp3stream.Search.SearchFragment;

public class TestFragment extends Fragment {
    private int num;

    public TestFragment() {}

//    public TestFragment(int num) {
//        this.num = num;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        if (savedInstanceState != null) {
//            num = savedInstanceState.getInt("num");
//            Log.d("RESTOR", "restore " + num);
//        }

        Bundle args = getArguments();
        num = args.getInt("num");


        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("num", num);
//        Log.d("RESTOR", "save " + num);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.nav_back_button).setOnClickListener(v -> {
            SearchFragment.getInstance().popBackStack();
//            getParentFragment().getActivity().onBackPressed();
        });
        ((Button) view.findViewById(R.id.open_fragment_button)).setText("Open " + num);
        view.findViewById(R.id.open_fragment_button).setOnClickListener(v -> {
            Fragment newFragment = new TestFragment();
            Bundle args = new Bundle();
            args.putInt("num", num + 1);
            newFragment.setArguments(args);
            SearchFragment.getInstance().addToBackStack(newFragment);
        });
    }
}
