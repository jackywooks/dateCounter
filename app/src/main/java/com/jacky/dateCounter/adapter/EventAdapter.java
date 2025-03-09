package com.jacky.dateCounter.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jacky.dateCounter.R;
import com.jacky.dateCounter.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EventAdapter
        extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
    );
    private OnEventLongClickListener longClickListener;

    public interface OnEventLongClickListener {
        void onEventLongClick(Event event);
    }

    public void setOnEventLongClickListener(OnEventLongClickListener listener) {
        this.longClickListener = listener;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull EventViewHolder holder) {
        super.onViewRecycled(holder);
        holder.stopTimer();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView dateText;
        private final TextView countNumberText;
        private final TextView descriptionText;
        private final TextView timeText;
        private final TextView timeCountdownText;
        private final SimpleDateFormat timeFormat = new SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
        );
        private final View circleBackground;
        private final android.os.Handler timerHandler = new android.os.Handler();
        private Runnable timerRunnable;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.eventName);
            dateText = itemView.findViewById(R.id.eventDate);
            countNumberText = itemView.findViewById(R.id.eventCountNumber);
            descriptionText = itemView.findViewById(R.id.eventDescription);
            timeText = itemView.findViewById(R.id.eventTime);
            timeCountdownText = itemView.findViewById(R.id.eventTimeCountdown);
            circleBackground = itemView.findViewById(R.id.circleBackground);

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                    longClickListener.onEventLongClick(events.get(position));
                    return true;
                }
                return false;
            });
        }

        void bind(Event event) {
            // Stop any existing timer first
            stopTimer();

            nameText.setText(event.getName());
            dateText.setText(dateFormat.format(new Date(event.getTimestamp())));

            circleBackground.setBackgroundResource(
                    event.isStarred()
                            ? R.drawable.star_background
                            : R.drawable.circle_background
            );

            // Set day number and label color
            int dayTextColor = event.isStarred()
                    ? itemView.getContext().getColor(android.R.color.black)
                    : itemView.getContext().getColor(android.R.color.white);

            countNumberText.setTextColor(dayTextColor);
            TextView daysLabel = itemView.findViewById(R.id.daysLabel);
            daysLabel.setTextColor(dayTextColor);

            // Keep other text black
            nameText.setTextColor(
                    itemView.getContext().getColor(android.R.color.black)
            );
            dateText.setTextColor(
                    itemView.getContext().getColor(android.R.color.black)
            );
            timeText.setTextColor(
                    itemView.getContext().getColor(android.R.color.black)
            );


            descriptionText.setTextColor(
                    itemView.getContext().getColor(android.R.color.black)
            );

            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                descriptionText.setVisibility(View.VISIBLE);
                descriptionText.setText(event.getDescription());
            } else {
                descriptionText.setVisibility(View.GONE);
            }

            long diff = System.currentTimeMillis() - event.getTimestamp();
            boolean isFuture = diff < 0;
            long days = TimeUnit.MILLISECONDS.toDays(Math.abs(diff));

            MaterialCardView cardView = (MaterialCardView) itemView;
            cardView.setCardBackgroundColor(
                    itemView
                            .getContext()
                            .getColor(
                                    isFuture ? R.color.primary_container : R.color.secondary_container
                            )
            );

            countNumberText.setText(String.valueOf(days));
            daysLabel.setText(isFuture ? "DAYS LEFT" : "DAYS AGO");

            if (!event.isDateOnly()) {
                timeText.setText(timeFormat.format(new Date(event.getTimestamp())));
                startTimer(event);
            } else {
                timeText.setText("");
                timeCountdownText.setText("");
            }
        }

        private void startTimer(Event event) {
            timerRunnable =
                    new Runnable() {
                        @Override
                        public void run() {
                            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                                stopTimer();
                                return;
                            }

                            long currentDiff =
                                    System.currentTimeMillis() - event.getTimestamp();
                            long remainingMillis = Math.abs(currentDiff);

                            long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
                            long minutes =
                                    TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60;
                            long seconds =
                                    TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60;

                            String countdownText = String.format(
                                    Locale.getDefault(),
                                    "%02d:%02d:%02d",
                                    hours,
                                    minutes,
                                    seconds
                            );

                            timeCountdownText.setText(countdownText);
                            timerHandler.postDelayed(this, 1000);
                        }
                    };
            timerHandler.post(timerRunnable);
        }

        void stopTimer() {
            if (timerRunnable != null) {
                timerHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }
        }
    }
}
