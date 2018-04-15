package com.singreading.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.singreading.DetailActivity;
import com.singreading.MainActivity;
import com.singreading.R;
import com.singreading.model.Lyric;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;
import static com.singreading.MainActivity.EXTRA_LYRIC;
import static com.singreading.MainActivity.selectedLyric;

/**
 * Implementation of App Widget functionality.
 */
public class SingreadingAppWidget extends AppWidgetProvider {

    private static final String TAG = "AppWidget";

    public static final String ACTION_LYRIC_CHANGED = "com.singreading.LYRIC_CHANGED";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.singreading_app_widget);

        Log.e(TAG, "EXTRA_APPWIDGET_IDS: " + appWidgetId);
        Intent intent;

        if (MainActivity.selectedLyric != null) {
            intent = new Intent(context.getApplicationContext(), DetailActivity.class);
            views.setTextViewText(R.id.appwidget_text, MainActivity.selectedLyric.getAllLyric());
            Log.e(TAG, "selectedLyric: " + MainActivity.selectedLyric.getName());
        }
        else {
            intent = new Intent(context.getApplicationContext(), MainActivity.class);
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.widget_without_lyric));
            Log.e(TAG, "selectedLyric is null ");
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);
        // Update the widgets via the service
        //context.startService(intent);

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
        super.onReceive(context, intent);
        Lyric receivedLyric = MainActivity.selectedLyric;
        if (receivedLyric != null)
            Log.e(TAG, "Lyric received: " + receivedLyric.getName());

    }
}

