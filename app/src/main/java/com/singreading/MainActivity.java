package com.singreading;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.singreading.model.Lyric;
import com.singreading.util.NetworkUtils;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LyricsAdapter.LyricsAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Lyric>> {

    private static final String TAG = "MainActivity";

    static final String EXTRA_LYRIC = "LYRIC";

    private EditText artistEditText;
    private EditText musicEditText;
    private RecyclerView mRecyclerView;
    LyricsAdapter mLyricsAdapter;

    private URL lyricURL;

    private Lyric lyric;

    private static final int LYRICS_LOADER = 332;
    private DatabaseReference mDatabase;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Layout
        artistEditText = findViewById(R.id.editext_artist_name);
        musicEditText = findViewById(R.id.editext_music_name);
        mRecyclerView = findViewById(R.id.rv_lyrics_main);

        //Recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mLyricsAdapter = new LyricsAdapter(this, this);
        mRecyclerView.setAdapter(mLyricsAdapter);

        //User
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Database
        mDatabase = FirebaseDatabase.getInstance().getReference("lyrics")
                .child(firebaseUser.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e(TAG, "onStart");

        final List<Lyric> lyrics = new ArrayList<Lyric>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lyrics.clear();

                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot lyricSnapshot : dataSnapshot.getChildren()) {
                        Lyric lyricFromFavorites = lyricSnapshot.getValue(Lyric.class);
                        lyrics.add(lyricFromFavorites);
                    }
                }
                else {
                    Log.e(TAG, "Empty favorites: ");
                    Lyric emptyLyric = new Lyric();
                    emptyLyric.setArtist(getString(R.string.empty_favorites_lyric));
                    emptyLyric.setName("");
                    lyrics.add(emptyLyric);
                }

                mLyricsAdapter.setLyricsData(lyrics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });

    }

    public void searchLyric(View v) {
        lyric = new Lyric();

        String rawArtist = artistEditText.getText().toString();
        String artist = rawArtist.substring(0, 1).toUpperCase() + rawArtist.substring(1);
        String rawName = musicEditText.getText().toString();
        String name = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);

        if (artist.equals("") || name.equals("")) {
            Log.e(TAG, "Form input empty!");
            Snackbar.make(v, "Both fields should be filled", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            lyric.setArtist(artist);
            lyric.setName(name);

            getSupportLoaderManager().restartLoader(LYRICS_LOADER, null, this);
        }
    }

    @Override
    public Loader<List<Lyric>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<Lyric>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                /*
                if (args == null)
                    return;
                    */


                //mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<Lyric> loadInBackground() {

                List<Lyric> lyrics = new ArrayList<Lyric>();

                // TRY TO FETCH LYRIC-API
                Log.e(TAG, "Fetching lyric-api...");
                fetchLyricApi();
                if (lyric != null && !lyric.getAllLyric().equals("")) {
                    lyrics.add(lyric);
                    return lyrics;
                }

                // TRY ANOTHER SOURCE OF LYRIC
                else {
                    Log.e(TAG, "Fetching MM...");
                    fetchMusixMatch();

                    if (lyric != null) {
                        lyrics.add(lyric);
                        return lyrics;
                    }
                    else {
                        Log.e(TAG, "Could not find on any source!");
                        return null;
                    }
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Lyric>> loader, List<Lyric> lyrics) {
        Log.e(TAG, "onLoadFinished");

        if (lyrics != null) {

            Intent intentDetail = new Intent(MainActivity.this, DetailActivity.class);
            intentDetail.putExtra(MainActivity.EXTRA_LYRIC, lyrics.get(0));
            startActivity(intentDetail);
        } else {
            Log.e(TAG, "Should stay in MainActivity: ");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Lyric>> loader) {
        Log.e(TAG, "onLoaderReset");

    }

    public void fetchLyricApi() {

        lyricURL = NetworkUtils.buildUrlForLyricApi(lyric.getArtist(), lyric.getName());
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(lyricURL);

            String lyricfromJson = NetworkUtils.getLyricFromJson(MainActivity.this, response);

            if (lyricfromJson != null) {
                lyric.setAllLyric(lyricfromJson);
                lyric.generateId();
            } else {
                lyric.setAllLyric(lyricfromJson);
                Log.e(TAG, "Error. Not found lyric?");
            }

        } catch (IOException e) {
            Log.e(TAG, "Error obtaining network response");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Error converting JSON");
            e.printStackTrace();
        }


    }

    public void fetchMusixMatch() {

        lyricURL = NetworkUtils.buildUrlForTest(lyric);

        try {
            String response = NetworkUtils.getResponseFromHttpUrl(lyricURL);

            if (response != null) {

                Document doc = Jsoup.parse(response);
                Elements content = doc.getElementsByClass("mxm-lyrics__content ");
                Elements titles = doc.getElementsByClass("mxm-track-title__track ");

                StringBuilder allTitles = new StringBuilder();
                for (Element tit : titles)
                    allTitles.append(tit.wholeText());

                String title = allTitles.substring(6);
                Log.e(TAG, "MM title: " + title);

                if (!title.equals(lyric.getName())) {
                    Log.e(TAG, "MM found another music! Should not display");
                    lyric = null;
                    return;
                }

                StringBuilder allLyric = new StringBuilder();
                for (Element aux : content)
                    allLyric.append(aux.wholeText());

                lyric.setAllLyric(allLyric.toString());
                Log.e(TAG, "Lyric returning from mm: " + lyric.getAllLyric());

            } else {
                Log.e(TAG, "Not found on MM" );
                lyric = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining network response");
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(Lyric lyricDetails) {

        if (lyricDetails.getArtist().equals(getString(R.string.empty_favorites_lyric))) {
            Toast.makeText(this, "Do you have a favorite music? =)",
                    Toast.LENGTH_SHORT).show();

        }
        else {
            Intent intentDetail = new Intent(this, DetailActivity.class);
            intentDetail.putExtra(MainActivity.EXTRA_LYRIC, lyricDetails);
            startActivity(intentDetail);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
