package com.singreading.widget;

import android.app.IntentService;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.singreading.MainActivity;
import com.singreading.R;

import static android.app.Activity.RESULT_OK;

public class UpdateWidgetService extends IntentService {

    private static final String TAG = "AppWidget";

    public static final String ACTION_UPDATE_WIDGET_LYRIC = "com.singreading.action.update_widget_lyric";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public UpdateWidgetService() { super("PlantWateringService");   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();


        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null)
            Log.e("service", "action " + intent.getAction());
    }
}
