package com.singreading.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabriel on 08/04/18.
 */

public class Lyric implements Parcelable {


    String artist;
    String name;
    String allLyric;

    public Lyric() {}

    public Lyric(String artist, String name, String allLyric){
        this.artist = artist;
        this.name = name;
        this.allLyric = allLyric;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getAllLyric() {
        return allLyric;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllLyric(String allLyric) {
        this.allLyric = allLyric;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.getArtist());
        out.writeString(this.getName());
        out.writeString(this.getAllLyric());
    }

    public static final Parcelable.Creator<Lyric> CREATOR
            = new Parcelable.Creator<Lyric>() {
        public Lyric createFromParcel(Parcel in) {
            return new Lyric(in);
        }

        public Lyric[] newArray(int size) {
            return new Lyric[size];
        }
    };

    private Lyric(Parcel in) {
        artist = in.readString();
        name = in.readString();
        allLyric = in.readString();
    }
}
