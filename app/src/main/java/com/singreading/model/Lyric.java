package com.singreading.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by gabriel on 08/04/18.
 */

@IgnoreExtraProperties
public class Lyric implements Parcelable {

    Long id;
    String artist;
    String name;
    String allLyric;

    public Lyric() {}

    public Lyric(Long id, String artist, String name, String allLyric){
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.allLyric = allLyric;
    }

    public Long getId() { return id; }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getAllLyric() {
        return allLyric;
    }

    public void setId(Long id) { this.id = id; }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllLyric(String allLyric) {
        this.allLyric = allLyric;
    }

    public void generateId() {
        int h = 0;
        for (int i = 0; i < getArtist().length(); i++) {
            h = 31 * h + getArtist().charAt(i);
        }
        for (int i = 0; i < getName().length(); i++) {
            h = 31 * h + getName().charAt(i);
        }

        if (h < 0)
            h = Math.abs(h);

        setId(Long.valueOf(h));
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(this.getId());
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
        id = in.readLong();
        artist = in.readString();
        name = in.readString();
        allLyric = in.readString();
    }

    /*
    @Override
    public String toString() {
        return this.getName();
    }
    */
}
