package com.jacky.dateCounter.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RemoteViews;

import com.jacky.dateCounter.MainActivity;
import com.jacky.dateCounter.R;
import com.jacky.dateCounter.data.EventDatabase;
import com.jacky.dateCounter.model.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class EventWidgetProvider extends AppWidgetProvider {
    public static final String PREFS_NAME = "com.jacky.dateCounter.widget";
    public static final String PREF_PREFIX_KEY = "widget_event_id_";

    @Override
    public void onUpdate(
            Context context,
            AppWidgetManager appWidgetManager,
            int[] appWidgetIds
    ) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(
            Context context,
            AppWidgetManager appWidgetManager,
            int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE);

        int eventId = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_event
        );

        // Create intent to launch MainActivity
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        if (eventId != -1) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Event event = EventDatabase
                        .getInstance(context)
                        .eventDao()
                        .getEventById(eventId);

                if (event != null) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        views.setTextViewText(R.id.widget_event_name, event.getName());
                        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                            views.setTextViewText(
                                    R.id.widget_event_description,
                                    event.getDescription()
                            );
                            views.setViewVisibility(R.id.widget_event_description, View.VISIBLE);

                        } else {
                            views.setViewVisibility(R.id.widget_event_description, View.GONE);
                        }


                        long diff = System.currentTimeMillis() - event.getTimestamp();
                        boolean isFuture = diff < 0;
                        long days = TimeUnit.MILLISECONDS.toDays(Math.abs(diff));

                        views.setTextViewText(R.id.widget_days, String.valueOf(days));
                        views.setTextViewText(R.id.widget_days_label, isFuture ? "DAYS LEFT" : "DAYS AGO");

                        // Update widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    });
                }
            });
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit();

        for (int appWidgetId : appWidgetIds) {
            editor.remove(PREF_PREFIX_KEY + appWidgetId);
        }

        editor.apply();
    }

    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
        // Set up alarm for widget updates

        Intent intent = new Intent(context, EventWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE
        );

        alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis(),
                TimeUnit.MINUTES.toMillis(30), // Update every 30 minutes
                pendingIntent
        );

    }

    @Override
    public void onDisabled(Context context) {
        // Cancel the alarm when the last widget is removed
        Intent intent = new Intent(context, EventWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        super.onDisabled(context);

    }

    /**
     * Update all active widgets
     * @param context The application context
     */
    public static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, EventWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Trigger a manual update
        Intent updateIntent = new Intent(context, EventWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(updateIntent);
    }

}