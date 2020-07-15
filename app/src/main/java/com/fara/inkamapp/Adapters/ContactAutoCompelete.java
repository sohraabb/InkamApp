package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fara.inkamapp.Models.ContactList;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAutoCompelete extends ArrayAdapter<ContactList> {
    private ArrayList<ContactList> items, tempItems, suggestions;
    private LayoutInflater mInflater;
    private ContactCustomListener mClickListener;
    private Context context;
    public interface ContactCustomListener {
        void customOnItemClick(ContactList c);
    }

    public void setClickListener(ContactCustomListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public ContactAutoCompelete(Context _context, ArrayList<ContactList> serviceTypes) {
        super(_context,0,serviceTypes);
        this.mInflater = LayoutInflater.from(_context);
        this.items = serviceTypes;
        this.context = _context;
        tempItems = new ArrayList<ContactList>(items); // this makes the difference.
        suggestions = new ArrayList<ContactList>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // View views = View.inflate(context, R.layout.adapter_spinner, null);
        //   View views =   mInflater.inflate(R.layout.adapter_spinner, viewGroup, false);
        //  TextView textView = (TextView) views.findViewById(R.id.tvContactListName);
        //  textView.setText(items.get(i).get_name());
        View views = view;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            views = mInflater.inflate(R.layout.adapter_provinces, viewGroup, false);
        }
        ContactList ContactList = items.get(i);
        if (ContactList != null) {
            TextView lblName = (TextView) views.findViewById(R.id.tv_province_name);
            TextView lblPhone = (TextView) views.findViewById(R.id.tv_phone);
            if (lblName != null)
                lblName.setText(ContactList.get_name());
            lblPhone.setText(ContactList.get_phone());
            lblPhone.setVisibility(View.VISIBLE);
        }
        return views;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ContactList) resultValue).get_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ContactList ContactList : tempItems) {
                    if (ContactList.get_name().toLowerCase().contains(constraint.toString().toLowerCase())||
                            ContactList.get_phone().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(ContactList);
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ContactList> filterList = (ArrayList<ContactList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ContactList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
