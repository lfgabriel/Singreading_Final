package com.singreading.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.singreading.DetailActivity;
import com.singreading.MainActivity;
import com.singreading.R;

/**
 * Created by gabriel on 14/04/18.
 */

public class SingreadingAppWidgetConfigure extends Activity {

    private static final String TAG = "AppWidgetConfigure";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        setContentView(R.layout.singreading_app_widget);
        TextView textView = (TextView) findViewById(R.id.appwidget_text);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.singreading_app_widget);

        if (MainActivity.selectedLyric != null) {
            views.setTextViewText(R.id.appwidget_text, MainActivity.selectedLyric.getAllLyric());
        }
        else {
            views.setTextViewText(R.id.appwidget_text, getResources().getString(R.string.widget_without_lyric));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
