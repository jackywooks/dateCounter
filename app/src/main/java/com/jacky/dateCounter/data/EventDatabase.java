package com.jacky.dateCounter.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.jacky.dateCounter.model.Event;

@Database(entities = { Event.class }, version = 1)
public abstract class EventDatabase extends RoomDatabase {

  private static EventDatabase instance;
  public abstract EventDao eventDao();

  public static synchronized EventDatabase getInstance(Context context) {
    if (instance == null) {
      instance =
        Room
          .databaseBuilder(
            context.getApplicationContext(),
            EventDatabase.class,
            "event_database"
          )
          .build();
    }
    return instance;
  }
}
