package com.jacky.dateCounter;
import com.jacky.dateCounter.widget.EventWidgetProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.jacky.dateCounter.adapter.EventAdapter;
import com.jacky.dateCounter.data.EventDatabase;
import com.jacky.dateCounter.model.Event;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EventAdapter adapter;
    private EventDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = EventDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        setupRecyclerView();
        setupAddButton();
        observeEvents();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new EventAdapter();
        adapter.setOnEventLongClickListener(this::showEditEventDialog);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAddButton() {
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showAddEventDialog());
    }

    private void observeEvents() {
        database
                .eventDao()
                .getAllEvents()
                .observe(
                        this,
                        events -> {
                            adapter.setEvents(events);
                        }
                );
    }

    private void showAddEventDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null);
        TextInputEditText nameInput = dialogView.findViewById(R.id.eventNameInput);
        TextInputEditText descriptionInput = dialogView.findViewById(R.id.eventDescriptionInput);
        Button dateTimeButton = dialogView.findViewById(R.id.dateTimeButton);
        SwitchMaterial dateOnlySwitch = dialogView.findViewById(R.id.dateOnlySwitch);
        SwitchMaterial starredSwitch = dialogView.findViewById(R.id.starredSwitch);

        final Calendar calendar = Calendar.getInstance();

        dateTimeButton.setOnClickListener(v -> {
            if (dateOnlySwitch.isChecked()) {
                showDatePicker(calendar);
            } else {
                showDateTimePicker(calendar);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Add New Event")
                .setView(dialogView)
                .setPositiveButton(
                        "Add",
                        (dialog, which) -> {
                            String name = nameInput.getText().toString();
                            String description = descriptionInput.getText().toString();

                            if (dateOnlySwitch.isChecked()) {
                                // Set time to start of day
                                calendar.set(Calendar.HOUR_OF_DAY, 0);
                                calendar.set(Calendar.MINUTE, 0);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                            }

                            long timestamp = calendar.getTimeInMillis();
                            Event event = new Event(
                                    name,
                                    timestamp,
                                    description,
                                    dateOnlySwitch.isChecked()
                            );
                            event.setStarred(starredSwitch.isChecked());

                            executorService.execute(() -> {
                                database.eventDao().insert(event);
                            });
                        }
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDatePicker(Calendar calendar) {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
                .show();
    }

    private void showDateTimePicker(Calendar calendar) {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    new TimePickerDialog(
                            this,
                            (view2, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    )
                            .show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
                .show();
    }

    private void showEditEventDialog(Event event) {
        // View Binding
        View dialogView = LayoutInflater
                .from(this)
                .inflate(R.layout.dialog_add_event, null);
        TextInputEditText nameInput = dialogView.findViewById(R.id.eventNameInput);
        TextInputEditText descriptionInput = dialogView.findViewById(
                R.id.eventDescriptionInput
        );
        Button dateTimeButton = dialogView.findViewById(R.id.dateTimeButton);
        SwitchMaterial dateOnlySwitch = dialogView.findViewById(
                R.id.dateOnlySwitch
        );
        SwitchMaterial starredSwitch = dialogView.findViewById(R.id.starredSwitch);

        // Pre-fill the existing data
        nameInput.setText(event.getName());
        descriptionInput.setText(event.getDescription());
        dateOnlySwitch.setChecked(event.isDateOnly());
        starredSwitch.setChecked(event.isStarred());

        // Create a new Event for updating to avoid modifying the original
        Event updatedEvent = new Event(
                event.getName(),
                event.getTimestamp(),
                event.getDescription(),
                event.isDateOnly()
        );
        updatedEvent.setId(event.getId());
        updatedEvent.setStarred(event.isStarred());

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getTimestamp());

        dateTimeButton.setOnClickListener(v -> {
            if (dateOnlySwitch.isChecked()) {
                showDatePicker(calendar);
            } else {
                showDateTimePicker(calendar);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Edit Event")
                .setView(dialogView)
                .setPositiveButton(
                        "Save",
                        (dialog, which) -> {
                            String name = nameInput.getText().toString();
                            String description = descriptionInput.getText().toString();

                            if (dateOnlySwitch.isChecked()) {
                                calendar.set(Calendar.HOUR_OF_DAY, 0);
                                calendar.set(Calendar.MINUTE, 0);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                            }

                            updatedEvent.setName(name);
                            updatedEvent.setDescription(description);
                            updatedEvent.setTimestamp(calendar.getTimeInMillis());
                            updatedEvent.setDateOnly(dateOnlySwitch.isChecked());
                            updatedEvent.setStarred(starredSwitch.isChecked());

                            executorService.execute(() -> {
                                database.eventDao().update(updatedEvent);
                            });
                        }
                )
                .setNegativeButton("Cancel", null)
                .setNeutralButton(
                        "Delete",
                        (dialog, which) -> {
                            new AlertDialog.Builder(this)
                                    .setTitle("Delete Event")
                                    .setMessage("Are you sure you want to delete this event?")
                                    .setPositiveButton(
                                            "Delete",
                                            (dialogInterface, i) -> {
                                                executorService.execute(() -> {
                                                    database.eventDao().delete(event);
                                                });
                                            }
                                    )
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                )
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventWidgetProvider.updateAllWidgets(this);
    }
}
