package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.fara.inkamapp.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportCityAdapter  extends ArrayAdapter<String>  {
    private List<String> items, tempItems, suggestions;
    private LayoutInflater mInflater;
    private CityCustomListener mClickListener;
    private Context context;
 private    Map<String,String> hashmap;



    public interface CityCustomListener {
        void customOnItemClick(String key);
    }

    public void setClickListener(CityCustomListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public AirportCityAdapter(Context _context, Map<String,String> serviceTypes,List<String> list ) {
        super(_context,0, list);

hashmap=serviceTypes;
        this.mInflater = LayoutInflater.from(_context);
        this.items = list;
        this.context = _context;
        tempItems = new ArrayList<>(items);//HashMap<String,String>(items); // this makes the difference.
        suggestions = new ArrayList<>();//HashMap<String,String>();
    }

   /* @Override
    public String getItem(int position) {
        return (String)items.values().toArray()[position];
    }*/
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
        final String key=(String)hashmap.keySet().toArray()[i];
        String city = hashmap.get(key);
        if (city != null) {
            TextView lblName = (TextView) views.findViewById(R.id.tvCityName);
            if (lblName != null)
                lblName.setText(city);
        }
        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.customOnItemClick(key);
            }
        });
        return views;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str ="";
            try {
                 str = ((String) resultValue);
                return str;
            }catch (Exception e){
                e.toString();
            }
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
         if (constraint != null) {
                suggestions.clear();
                for (String city : tempItems) {
                    if (city.toLowerCase().contains(constraint.toString().toLowerCase())) {
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

          //  list.addAll(results.values);
            try {
                List<String> filterList = (List<String>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (String people : filterList) {
                        add(people);
                        notifyDataSetChanged();
                    }
                }
            }catch (Exception e){
                e.toString();
            }
        }
    };


}
