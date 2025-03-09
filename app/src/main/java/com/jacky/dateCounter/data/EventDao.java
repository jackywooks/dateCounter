package com.jacky.dateCounter.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.jacky.dateCounter.model.Event;
import java.util.List;

@Dao
public interface EventDao {
  @Insert
  void insert(Event event);

  @Update
  void update(Event event);

  @Query("SELECT * FROM events ORDER BY starred DESC, timestamp")
  LiveData<List<Event>> getAllEvents();

  @Delete
  void delete(Event event);

  @Query("SELECT * FROM events WHERE id = :id")
  Event getEventById(int id);
}
