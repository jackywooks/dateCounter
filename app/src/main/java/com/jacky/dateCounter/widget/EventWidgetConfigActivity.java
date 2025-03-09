package com.jacky.dateCounter.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.jacky.dateCounter.R;
import com.jacky.dateCounter.data.EventDatabase;

public class EventWidgetConfigActivity extends AppCompatActivity {

  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setResult(RESULT_CANCELED);
    setContentView(R.layout.activity_widget_config);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      appWidgetId =
        extras.getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID
        );
    }

    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
      return;
    }

    EventDatabase
      .getInstance(this)
      .eventDao()
      .getAllEvents()
      .observe(
        this,
        events -> {
          ListView listView = findViewById(R.id.event_list);
          EventWidgetConfigAdapter adapter = new EventWidgetConfigAdapter(
            this,
            events
          );
          listView.setAdapter(adapter);

          listView.setOnItemClickListener((parent, view, position, id) -> {
            int eventId = events.get(position).getId();
            SharedPreferences.Editor prefs = getSharedPreferences(
              EventWidgetProvider.PREFS_NAME,
              MODE_PRIVATE
            )
              .edit();
            prefs.putInt(
              EventWidgetProvider.PREF_PREFIX_KEY + appWidgetId,
              eventId
            );
            prefs.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(
              this
            );
            EventWidgetProvider.updateAppWidget(
              this,
              appWidgetManager,
              appWidgetId
            );

            Intent resultValue = new Intent();
            resultValue.putExtra(
              AppWidgetManager.EXTRA_APPWIDGET_ID,
              appWidgetId
            );
            setResult(Activity.RESULT_OK, resultValue);
            finish();
          });
        }
      );
  }
}
