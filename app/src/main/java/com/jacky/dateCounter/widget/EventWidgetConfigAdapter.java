package com.jacky.dateCounter.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.jacky.dateCounter.R;
import com.jacky.dateCounter.model.Event;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventWidgetConfigAdapter extends ArrayAdapter<Event> {

  private final SimpleDateFormat dateFormat;

  public EventWidgetConfigAdapter(Context context, List<Event> events) {
    super(context, 0, events);
    dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
  }

  @NonNull
  @Override
  public View getView(
    int position,
    View convertView,
    @NonNull ViewGroup parent
  ) {
    if (convertView == null) {
      convertView =
        LayoutInflater
          .from(getContext())
          .inflate(android.R.layout.simple_list_item_2, parent, false);
    }

    Event event = getItem(position);
    if (event != null) {
      TextView text1 = convertView.findViewById(android.R.id.text1);
      TextView text2 = convertView.findViewById(android.R.id.text2);

      text1.setText(event.getName());
      text2.setText(dateFormat.format(event.getTimestamp()));
    }

    return convertView;
  }
}
