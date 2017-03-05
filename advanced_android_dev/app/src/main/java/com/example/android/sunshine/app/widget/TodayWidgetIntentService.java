package com.example.android.sunshine.app.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by lara on 04.03.17.
 */

public class TodayWidgetIntentService extends IntentService {
    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_SHORT_DESC = 1;
    private static final int INDEX_MAX_TEMP = 2;
    private static final String LOG_TAG = TodayWidgetIntentService.class.getCanonicalName();

    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                TodayWidgetProvider.class));
        // Get today's data from the ContentProvider
        String location = Utility.getPreferredLocation(this);
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                location, System.currentTimeMillis());
        Cursor data = getContentResolver().query(weatherForLocationUri, FORECAST_COLUMNS, null,
                null, WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        // Extract the weather data from the Cursor
        int weatherId = data.getInt(INDEX_WEATHER_ID);
        int weatherArtResourceId = Utility.getArtResourceForWeatherCondition(weatherId);
        String description = data.getString(INDEX_SHORT_DESC);
        double maxTemp = data.getDouble(INDEX_MAX_TEMP);
        String formattedMaxTemperature = Utility.formatTemperature(this, maxTemp);
        data.close();


        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    getPackageName(),
                    getWidgetLayout(appWidgetManager, appWidgetId));

            views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
            views.setTextViewText(R.id.widget_high_temperature, formattedMaxTemperature);
            views.setTextViewText(R.id.widget_description, description);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private int getWidgetLayout(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle appWidgetOptions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            int widthInDp = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int widthInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp,
                    displayMetrics);
            Log.v(LOG_TAG, "width pixels: " + String.valueOf(widthInPixels));
            if (widthInPixels >= getResources().getDimensionPixelSize(R.dimen.widget_today_large_width)) {
                Log.v(LOG_TAG, "pixels: " + String.valueOf(getResources().getDimensionPixelSize(R.dimen.widget_today_large_width)));
                Log.v(LOG_TAG, "layout widget large");
                return R.layout.widget_today_large;
            } else if (widthInPixels >= getResources().getDimensionPixelSize(R.dimen.widget_today_default_width)) {
                Log.v(LOG_TAG, "pixels: " + String.valueOf(getResources().getDimensionPixelSize(R.dimen.widget_today_default_width)));
                Log.v(LOG_TAG, "layout widget medium");
                return R.layout.widget_today;
            } else {
                Log.v(LOG_TAG, "layout widget small");
                return R.layout.widget_today_small;
            }
        }
        Log.v(LOG_TAG, "layout widget medium");
        return R.layout.widget_today;
    }
}
