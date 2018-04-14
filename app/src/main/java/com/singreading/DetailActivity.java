package com.singreading;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.singreading.model.Lyric;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG  = "DetailActivity";

    private Lyric lyric;

    private ScrollView mDetailScrollView;
    private TextView lyricTextView;
    private TextView detailTitle;
    FloatingActionButton fab;
    boolean isFavorite;

    private DatabaseReference mDatabase;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite(lyric, view);
            }
        });

        lyric = getIntent().getParcelableExtra(MainActivity.EXTRA_LYRIC);

        mDetailScrollView = (ScrollView) findViewById(R.id.sv_detail_activity);
        detailTitle = (TextView) findViewById(R.id.tv_detail_title);

        //SET TEXT CAPITALIZED
        detailTitle.setText(lyric.getArtist().substring(0, 1).toUpperCase() + lyric.getArtist().substring(1)
                + " - " + lyric.getName().substring(0, 1).toUpperCase() + lyric.getName().substring(1));

        lyricTextView = (TextView) findViewById(R.id.tv_lyric);

        //GET USER AND DATABASE
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        lyricTextView.setText(lyric.getAllLyric());

        Intent intent = new Intent(SingreadingAppWidget.ACTION_LYRIC_CHANGED);
        intent.putExtra(MainActivity.EXTRA_LYRIC, lyric);
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase = FirebaseDatabase.getInstance().getReference("lyrics")
                .child(firebaseUser.getUid());

        final List<Lyric> lyrics = new ArrayList<Lyric>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lyrics.clear();

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot lyricSnapshot : dataSnapshot.getChildren()) {
                        Lyric lyricFromFavorites = lyricSnapshot.getValue(Lyric.class);
                        if (lyricFromFavorites.getId().equals(lyric.getId())) {
                            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.favorite_star_yellow));
                            isFavorite = true;
                            return;
                        }
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.favorite_star));
                    isFavorite = false;

                }
                else {
                    Log.e(TAG, "Empty favorites: ");
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.favorite_star));
                    isFavorite = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void favorite(Lyric lyric, View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference("lyrics")
                .child(firebaseUser.getUid());

        if (isFavorite) {
            mDatabase.child(String.valueOf(lyric.getId())).setValue(null);
            Snackbar.make(view, "Removed from the favorite list!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.favorite_star));
            isFavorite = false;
        }
        else {
            mDatabase.child(String.valueOf(lyric.getId())).setValue(lyric);

            Snackbar.make(view, "Added to the favorites!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.favorite_star_yellow));
            isFavorite = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION", new int[] {mDetailScrollView.getScrollX(), mDetailScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] scroll_positions = savedInstanceState.getIntArray("SCROLL_POSITION");

        if (mDetailScrollView != null) {
            mDetailScrollView .post(new Runnable() {
                @Override
                public void run() {
                    mDetailScrollView .scrollTo(scroll_positions[0], scroll_positions[1]);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Log.e(TAG, "onBackPressed");
    }
}
