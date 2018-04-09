package com.singreading;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.singreading.model.Lyric;
import com.singreading.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG  = "DetailActivity";

    private Lyric lyric;

    private TextView lyricTextView;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lyricTextView = (TextView) findViewById(R.id.tv_lyric);
        lyric = getIntent().getParcelableExtra(MainActivity.LYRIC);

        lyricTextView.setText(lyric.getAllLyric());
        setTitle(lyric.getArtist() + " - " + lyric.getName());

        favorite(lyric);
    }

    public void favorite(Lyric lyric) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());
        user.setuID(firebaseUser.getUid());

        List<Lyric> userLyrics = new ArrayList<Lyric>();
        userLyrics.add(lyric);
        user.setFavorites(userLyrics);

        //String databaseUser = "lyric-" + user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("lyrics").child(user.getuID()).setValue(user.getFavorites());
        Log.e(TAG, "Lyric saved");
    }

}
