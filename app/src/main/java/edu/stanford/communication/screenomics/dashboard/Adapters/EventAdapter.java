/**
 * EventAdapter.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.stanford.communication.screenomics.dashboard.PojoClass.EventDetails;
import edu.stanford.communication.screenomics.dashboard.R;
import edu.stanford.communication.screenomics.dashboard.Utils.DateTimeChecker;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Activity activity;
    private ArrayList<EventDetails> eventList;

    public EventAdapter(ArrayList<EventDetails> eventList, Activity activity) {
        this.eventList = eventList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_user_event_holder, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventDetails event = eventList.get(position);
        String eventTime = event.getEventTime();

        // Set event name and event time text
        holder.eventName.setText(event.getEventName() + "\n" + eventTime);

        // Bold event name and set dot color based on event age
        if (DateTimeChecker.isMoreThan24HoursAgo(eventTime)) {
            holder.eventName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.dotIndicator.setBackgroundResource(R.drawable.red_rect);
        } else {
            holder.eventName.setTypeface(Typeface.DEFAULT);
            holder.dotIndicator.setBackgroundResource(R.drawable.green_rect);
        }

        // Clear any tint that might be applied from view recycling
        if (holder.dotIndicator.getBackground() != null) {
            holder.dotIndicator.getBackground().setTintList(null);
        }

        // Set "x hours/minutes ago" text
        String timeAgo = DateTimeChecker.getTimeAgoString(eventTime);
        holder.eventTimeAgo.setText(timeAgo);
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventTimeAgo;
        View dotIndicator;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventTimeAgo = itemView.findViewById(R.id.event_time_ago);
            dotIndicator = itemView.findViewById(R.id.dot_indicator);
        }
    }
}
