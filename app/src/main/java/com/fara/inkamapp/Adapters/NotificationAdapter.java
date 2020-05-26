package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<NotificationList> notificationLists;
    private LayoutInflater mInflater;
    private ContactsAdapter.ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public NotificationAdapter(Context _context, ArrayList<NotificationList> _notificationList) {
        this.mInflater = LayoutInflater.from(_context);
        this.notificationLists = _notificationList;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_notification, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationList _notifications = notificationLists.get(position);

        if (_notifications.get_title() != null)
            holder.title.setText(_notifications.get_title());

        if (_notifications.get_dateTime() != null)
            holder.dateTime.setText(_notifications.get_dateTime());

        if (_notifications.get_message() != null)
            holder.message.setText(_notifications.get_message());

//        if(_cards.get_bankName() !=null)
//            if(_cards.get_bankName().equals("saman"))
//                holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));


    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != notificationLists ? notificationLists.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, dateTime, message;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_inkam_title);
            message = itemView.findViewById(R.id.tv_message);
            dateTime = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public NotificationList getItem(int id) {
        return notificationLists.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ContactsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
