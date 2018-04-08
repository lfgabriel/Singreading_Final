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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.singreading.model.Lyric;
import com.singreading.util.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Lyric>> {

    private static final String TAG  = "MainActivity";

    static final String LYRIC = "LYRIC";

    private EditText artistEditText;
    private EditText musicEditText;

    private URL lyricURL;
    private Lyric lyric = new Lyric();

    private static final int LYRICS_LOADER = 332;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        artistEditText = findViewById(R.id.editext_artist_name);
        musicEditText = findViewById(R.id.editext_music_name);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void searchLyric(View v){
        getSupportLoaderManager().initLoader(LYRICS_LOADER, null, this);
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
            public List<Lyric>loadInBackground() {

                String artist = artistEditText.getText().toString();
                String name = musicEditText.getText().toString();

                lyricURL = NetworkUtils.buildUrlForSearchingLyric(artist, name);

                String response = "";
                try {
                    List<Lyric> lyrics = new ArrayList<Lyric>();

                    response = NetworkUtils.getResponseFromHttpUrl(lyricURL);

                    String lyricfromJson = NetworkUtils.getLyricFromJson(MainActivity.this, response);

                    if (lyricfromJson != null) {
                        lyric.setAllLyric(lyricfromJson);
                        lyric.setArtist(artist);
                        lyric.setName(name);
                        lyrics.add(lyric);
                        return lyrics;
                    }
                    else
                        return null;

                } catch (IOException e) {
                    Log.e(TAG, "Error obtaining network response");
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    Log.e(TAG, "Error converting JSON");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Lyric>> loader, List<Lyric> lyrics) {

        if (lyrics != null)  {
            Log.e(TAG, "Lyric: " + lyrics.get(0).getAllLyric());
            Intent intentDetal = new Intent(MainActivity.this, DetailActivity.class);
            intentDetal.putExtra(MainActivity.LYRIC, lyrics.get(0));
            startActivity(intentDetal);
        }
        else
        Log.e(TAG, "Error with lyric: " + lyric.getAllLyric());
    }

    @Override
    public void onLoaderReset(Loader<List<Lyric>> loader) {

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
