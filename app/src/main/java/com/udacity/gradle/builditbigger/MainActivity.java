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

public class MainActivity extends AppCompatActivity implements OnJokeButtonListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
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

//    private void addFragment(){
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, new MainActivityFragment())
//                .commit();
//    }

    @Override
    public void onJokeButtonClicked(String joke) {
        Log.w(LOG_TAG, "onJokeButtonClicked");
        startJokeActivity(joke);
    }

    private void startJokeActivity(String joke){
        startActivity(new Intent(getBaseContext(), JokeActivity.class)
                .putExtra(JokeActivity.JOKE_KEY, joke));
    }

//    public ProgressBar getProgressBar() {
//        return mProgressBar;
//    }

    public void progressVisible() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void progressGone(){
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
