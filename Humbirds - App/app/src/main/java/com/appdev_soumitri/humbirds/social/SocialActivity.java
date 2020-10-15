package com.appdev_soumitri.humbirds.social;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alelak.soundroid.Soundroid;
import com.alelak.soundroid.models.Track;
import com.appdev_soumitri.humbirds.R;
import com.appdev_soumitri.humbirds.Urls;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


    }
}