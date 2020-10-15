package com.appdev_soumitri.humbirds.services;

import com.appdev_soumitri.humbirds.Urls;
import com.appdev_soumitri.humbirds.models.SongModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// this is interface for various queries to be made
// sample interface for hands on

public interface SCService {
    @GET("/tracks?client_id=" + Urls.CLIENT_ID)
    Call<List<SongModel>> getRecentTracks(@Query("created_at") String date);
}
