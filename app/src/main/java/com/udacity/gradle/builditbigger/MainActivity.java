package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.androidlibrary.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
//import com.udacity.gradle.builditbigger.paid.MainActivityFragment;
import com.udacity.gradle.builditbigger.free.MainActivityFragment;

import static com.example.android.javajokes.Joker.JOKE_KEY;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnJokeButtonListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private InterstitialAd mInterstitialAd;
    private ProgressBar mProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        addFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MainActivityFragment())
                .commit();
    }

    @Override
    public void onJokeButtonClicked(final boolean isPaid) {
        if (!isPaid){
            initialiseInterstitialAd();
        }
        progressVisible();
        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(final String joke) {
                if (!joke.isEmpty()){
                    if (mInterstitialAd != null){
                        selectAdOptions(joke);
                    }else {
                        progressGone();
                        startJokeActivity(joke);
                    }
                }
            }
        });
    }

    private void startJokeActivity(String joke){
        startActivity(new Intent(getApplicationContext(), JokeActivity.class)
                .putExtra(JOKE_KEY, joke));
    }

    private void selectAdOptions(final String joke){
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                progressGone();
                showAd(mInterstitialAd);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                progressGone();
                startJokeActivity(joke);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startJokeActivity(joke);
            }
        });
    }

    private void progressVisible() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void progressGone(){
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showAd(InterstitialAd mInterstitialAd){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.w(LOG_TAG, "mInterstitialAd.show");
        } else {
            Log.w(LOG_TAG, "The interstitial wasn't loaded yet.");
        }
    }

    private void initialiseInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getBaseContext());
        mInterstitialAd.setAdUnitId(this.getString(R.string.interstitial_ad_unit_id));
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
