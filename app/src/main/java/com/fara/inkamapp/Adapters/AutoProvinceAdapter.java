package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
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
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class AutoProvinceAdapter extends ArrayAdapter<Province> {
    private List<Province> items, tempItems, suggestions;
    private LayoutInflater mInflater;
    private CityCustomListener mClickListener;
    private Context context;


    public interface CityCustomListener {
        void customOnItemClick(City city);
    }

    public void setClickListener(CityCustomListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public AutoProvinceAdapter(Context _context, List<Province> serviceTypes) {
        super(_context, 0, serviceTypes);
        this.mInflater = LayoutInflater.from(_context);
        this.items = serviceTypes;
        this.context = _context;
        tempItems = new ArrayList<Province>(items); // this makes the difference.
        suggestions = new ArrayList<Province>();
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
            views = mInflater.inflate(R.layout.adapter_spinner, viewGroup, false);
        }
        Province city = items.get(i);
        if (city != null) {
            TextView lblName = (TextView) views.findViewById(R.id.tvCityName);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/IRANSansMobile.ttf");
            lblName.setTypeface(typeface);
            if (lblName != null)
                lblName.setText(city.get_name());
        }
        return views;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Province) resultValue).get_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Province city : tempItems) {
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
            List<Province> filterList = (ArrayList<Province>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Province people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };


}
