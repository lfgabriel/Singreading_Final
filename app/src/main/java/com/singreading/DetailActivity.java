package com.singreading;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.singreading.model.Lyric;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private Lyric lyric;

    private TextView lyricTextView;

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
    }

}
