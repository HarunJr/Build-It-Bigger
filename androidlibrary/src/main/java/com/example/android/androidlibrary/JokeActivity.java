package com.example.android.androidlibrary;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.androidlibrary.databinding.ActivityJokeBinding;

public class JokeActivity extends AppCompatActivity {
    public static final String JOKE_KEY = "javaJoke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityJokeBinding mJokeBinding = DataBindingUtil.setContentView(this, R.layout.activity_joke);

        if (getIntent().getExtras() != null) {
            String joke  = getIntent().getStringExtra(JOKE_KEY);
            mJokeBinding.textView.setText(joke);

//            Log.w(LOG_TAG, "getDataFromMainActivity " + recipe.getName());
        }

    }
}
