package com.singreading;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.singreading.model.Lyric;

import static com.singreading.MainActivity.EXTRA_LYRIC;
import static com.singreading.MainActivity.selectedLyric;

/**
 * Implementation of App Widget functionality.
 */
public class SingreadingAppWidget extends AppWidgetProvider {

    private static final String TAG = "AppWidget";

    static final String ACTION_LYRIC_CHANGED = "com.singreading.LYRIC_CHANGED";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.e(TAG, "updateAppWidget");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.singreading_app_widget);


        if (MainActivity.selectedLyric != null) {
            CharSequence widgetText = selectedLyric.getAllLyric();
            views.setTextViewText(R.id.appwidget_text, widgetText);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive action: " + intent.getAction());
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_LYRIC_CHANGED)) {
            Lyric newLyric = intent.getParcelableExtra(MainActivity.EXTRA_LYRIC);
            Log.e(TAG, "Lyric widget: " + newLyric.getName());
        }
    }
}

