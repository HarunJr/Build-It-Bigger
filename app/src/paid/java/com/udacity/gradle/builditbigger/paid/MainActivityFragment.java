package com.udacity.gradle.builditbigger.paid;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.builditbigger.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.OnJokeButtonListener;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentMainBinding;
import com.udacity.gradle.builditbigger.onJokeReceived;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private OnJokeButtonListener jokeButtonListener;
    private static final boolean IS_PAID = true;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_main, container, false);
        FragmentMainBinding mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View root = mainBinding.getRoot();

        Button button = mainBinding.jokeButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJokeButtonClicked();
            }
        });

        return root;
    }

    private void onJokeButtonClicked(){
        ((MainActivity) getActivity()).progressVisible();
        if (jokeButtonListener != null){
            initNetworkConnection();
        }
    }

    private void initNetworkConnection() {
        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(final String joke) {
                if (!joke.isEmpty()) {
                    ((MainActivity) getActivity()).progressGone();
                    jokeButtonListener.onJokeButtonClicked(joke);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJokeButtonListener) {
            jokeButtonListener = (OnJokeButtonListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnJokeButtonListener");
        }
    }
}
