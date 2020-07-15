package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.ContactList;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<ContactList> contactLists;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public ContactsAdapter(Context _context, ArrayList<ContactList> _contactLists) {
        this.mInflater = LayoutInflater.from(_context);
        this.contactLists = _contactLists;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_contacts, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ContactList _contacts = contactLists.get(position);

        if (_contacts.get_name() != null)
            holder.name.setText(_contacts.get_name());

//        if (_contacts.get_phone() != null)
//            holder.expirationDate.setText(_contacts.get_phone());

//        if (_contacts.get_description() != null)
//            holder.name.setText(_contacts.get_description());

//        if(_cards.get_bankName() !=null)
//            if(_cards.get_bankName().equals("saman"))
//                holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));


    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != contactLists ? contactLists.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private CheckBox checkBox;
        private ImageView profilePic;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_contact_name);
            checkBox = itemView.findViewById(R.id.contact_checkbox);
            profilePic = itemView.findViewById(R.id.iv_contact_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ContactList getItem(int id) {
        return contactLists.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

