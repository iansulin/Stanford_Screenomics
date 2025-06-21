/**
 * EmailAdapter.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import edu.stanford.communication.screenomics.dashboard.AppActivity.ShowUserList;
import edu.stanford.communication.screenomics.dashboard.PojoClass.UserTickerInfo;
import edu.stanford.communication.screenomics.dashboard.R;
import edu.stanford.communication.screenomics.dashboard.Utils.CustomItemSorter;
import edu.stanford.communication.screenomics.dashboard.Utils.DateTimeChecker;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private List<UserTickerInfo> emailList;
    private Activity activity;

    private final String TAG = "EmailAdapter";

    public EmailAdapter(List<UserTickerInfo> emailList, Activity activity) {
        this.emailList = emailList;
        this.activity = activity;

        // Sort the list based on the custom logic
        CustomItemSorter.sortCustomItemList(emailList);
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_email_holder, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        UserTickerInfo user = emailList.get(position);

        Log.d(TAG, "user email " + user.getUserEmail() + " User Data" + user.getUserEventsData());

        // Set rectangle background based on active/inactive status
        if (user.getIsUserActiveInLast24Hours()) {
            holder.ShowUserActiveInActiveView.setBackgroundResource(R.drawable.green_rect);
            holder.emailTextView.setTypeface(Typeface.DEFAULT);
        } else {
            holder.ShowUserActiveInActiveView.setBackgroundResource(R.drawable.red_rect);
            holder.emailTextView.setTypeface(Typeface.DEFAULT_BOLD);
        }

        holder.emailTextView.setText(user.getUserEmail());

        // Get MostRecentEventTime string from user events data map
        Object mostRecentEventTimeObj = user.getUserEventsData().get("MostRecentEventTime");
        String mostRecentEventTimeStr = (mostRecentEventTimeObj != null) ? mostRecentEventTimeObj.toString() : null;

        // Use DateTimeChecker to get "time ago" string
        String timeAgo = DateTimeChecker.getTimeAgoString(mostRecentEventTimeStr);
        holder.timeAgoTextView.setText(timeAgo);

        holder.emailTextView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ShowUserList.class);
            intent.putExtra("Map", (Serializable) user.getUserEventsData());
            intent.putExtra("email", user.getUserEmail());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    static class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        View ShowUserActiveInActiveView;  // rectangle marker view
        TextView timeAgoTextView;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            ShowUserActiveInActiveView = itemView.findViewById(R.id.ShowUserActiveInActiveView);
            timeAgoTextView = itemView.findViewById(R.id.timeAgoTextView);
        }
    }
}
