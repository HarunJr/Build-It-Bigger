package com.udacity.gradle.builditbigger;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by HARUN on 2/18/2018.
 */
@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest{
    private CountDownLatch signal;
    private String mJoke = "";

    @Test
    public void doInBackground() throws Exception {
        signal = new CountDownLatch(1);
        new EndpointsAsyncTask().execute(new onJokeReceived() {
            @Override
            public void OnJokeReceivedListener(String joke) {
                mJoke = joke;
                signal.countDown();
            }
        });
        signal.await(10, TimeUnit.SECONDS);
        assertNotNull("joke is null", mJoke);

    }
}