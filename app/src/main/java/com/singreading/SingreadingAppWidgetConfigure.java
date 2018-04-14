package com.singreading;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by gabriel on 14/04/18.
 */

public class SingreadingAppWidgetConfigure extends Activity {

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

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();



    }
}
