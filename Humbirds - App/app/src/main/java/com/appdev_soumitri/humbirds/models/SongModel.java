package com.appdev_soumitri.humbirds.models;

import com.google.gson.annotations.SerializedName;

public class SongModel {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("user_id")
    private String mArtistid;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("streamable")
    private boolean mStreamable;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("genre")
    private String mGenre;

    @SerializedName("tag_list")
    private String mTagList;

    public String getArtistid() {
        return mArtistid;
    }

    public String getTagList() {
        return mTagList;
    }

    public String getGenre() {
        return mGenre;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }


}
