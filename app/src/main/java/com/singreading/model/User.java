package com.singreading.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 08/04/18.
 */

public class User implements Parcelable {

    String email;
    String name;
    String uID;

    List<Lyric> favorites;


    public User() { }

    public User(String email, String name, String uID) {
        this.email = email;
        this.name = name;
        this.uID = uID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getuID() {
        return uID;
    }

    public List<Lyric> getFavorites() {
        return favorites;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setFavorites(List<Lyric> favorites) {
        this.favorites = favorites;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.getEmail());
        out.writeString(this.getName());
        out.writeString(this.getuID());
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        email = in.readString();
        name = in.readString();
        uID = in.readString();
    }


}
