package com.udacity.gradle.builditbigger.free;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentMainBinding;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final boolean IS_PAID = false;
    private OnJokeButtonListener jokeButtonListener;

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
                Log.w(LOG_TAG, "onClick: ");
                onJokeButtonClicked(IS_PAID);
            }
        });

        AdView mAdView = mainBinding.adView;
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        return root;
    }


    private void onJokeButtonClicked(boolean isPaid){
        if (jokeButtonListener != null){
            jokeButtonListener.onJokeButtonClicked(isPaid);
        }
    }

    public interface OnJokeButtonListener {
        void onJokeButtonClicked(boolean isPaid);
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
