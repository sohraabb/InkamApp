package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.City;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {
    private List<City> items, tempItems, suggestions;
    private LayoutInflater mInflater;
    private CityCustomListener mClickListener;
    private Context context;


    public interface CityCustomListener {
        void customOnItemClick(City city);
    }

    public void setClickListener(CityCustomListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public CityAdapter(Context _context, List<City> serviceTypes) {
        super(_context,0,serviceTypes);
        this.mInflater = LayoutInflater.from(_context);
        this.items = serviceTypes;
        this.context = _context;
        tempItems = new ArrayList<City>(items); // this makes the difference.
        suggestions = new ArrayList<City>();
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
      //  TextView textView = (TextView) views.findViewById(R.id.tvCityName);
      //  textView.setText(items.get(i).get_name());
        View views = view;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            views =  mInflater.inflate(R.layout.adapter_spinner, viewGroup, false);
        }
        City city = items.get(i);
        if (city != null) {
            TextView lblName = (TextView) views.findViewById(R.id.tvCityName);
            if (lblName != null)
                lblName.setText(city.get_name());
        }
        return views;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((City) resultValue).get_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (City city : tempItems) {
                    if (city.get_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(city);
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
            List<City> filterList = (ArrayList<City>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (City people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };


}
