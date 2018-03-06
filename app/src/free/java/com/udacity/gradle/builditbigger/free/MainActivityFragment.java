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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
    private static final boolean IS_PAID = false;
    private OnJokeButtonListener jokeButtonListener;
    private InterstitialAd mInterstitialAd;

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
                onJokeButtonClicked();
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


    private void onJokeButtonClicked() {
        ((MainActivity) getActivity()).progressVisible();
        if (jokeButtonListener != null) {
            initialiseInterstitialAd();
            initNetworkConnection();
        }
    }

    private void initNetworkConnection() {
        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(final String joke) {
                if (!joke.isEmpty()) {
                    if (mInterstitialAd != null) {
                        selectAdOptions(joke, mInterstitialAd);
                    } else {
                        ((MainActivity) getActivity()).progressGone();
                        jokeButtonListener.onJokeButtonClicked(joke);
                    }
                }
            }
        });
    }

    private void selectAdOptions(final String joke, final InterstitialAd mInterstitialAd) {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                ((MainActivity) getActivity()).progressGone();
                showAd(mInterstitialAd);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                ((MainActivity) getActivity()).progressGone();
                jokeButtonListener.onJokeButtonClicked(joke);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                jokeButtonListener.onJokeButtonClicked(joke);
            }
        });
    }

    private void showAd(InterstitialAd mInterstitialAd) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.w(LOG_TAG, "mInterstitialAd.show");
        } else {
            Log.w(LOG_TAG, "The interstitial wasn't loaded yet.");
        }
    }

    private void initialiseInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(this.getString(R.string.interstitial_ad_unit_id));
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
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
