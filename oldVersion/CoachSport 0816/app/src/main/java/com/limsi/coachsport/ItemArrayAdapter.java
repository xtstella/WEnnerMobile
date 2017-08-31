package com.limsi.coachsport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter {
    private List valueList = new ArrayList();

    static class ItemViewHolder {
        TextView type;
        TextView date;
        TextView time;
        TextView calories;
        TextView distance;
        TextView mood;

        TextView date_heart_rate;
        TextView heart_rate;
    }

    public ItemArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public void add(String[] object) {
        valueList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public String[] getItem(int index) {
        return (String[]) this.valueList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        String[] stat = getItem(position);

        if (row == null) {
            if(stat.length > 5) {
                LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_history_layout, parent, false);
                viewHolder = new ItemViewHolder();
                viewHolder.type = (TextView) row.findViewById(R.id.type);
                viewHolder.date = (TextView) row.findViewById(R.id.date);
                viewHolder.time = (TextView) row.findViewById(R.id.time);
                viewHolder.calories = (TextView) row.findViewById(R.id.calories);
                viewHolder.distance = (TextView) row.findViewById(R.id.distance);
                viewHolder.date_heart_rate = (TextView) row.findViewById(R.id.date_heart_rate);
                viewHolder.heart_rate = (TextView) row.findViewById(R.id.heart_rate);
                viewHolder.mood = (TextView) row.findViewById(R.id.mood);
                row.setTag(viewHolder);

                viewHolder.type.setText(stat[0]);
                viewHolder.date.setText(stat[1]);
                viewHolder.time.setText(stat[6]);
                viewHolder.calories.setText(stat[5]);
                viewHolder.distance.setText(stat[4]);
                viewHolder.mood.setText(stat[2]);
            } else if (stat.length == 2){
                LayoutInflater inflater = (LayoutInflater) this.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_profile_layout, parent, false);
                viewHolder = new ItemViewHolder();
                viewHolder.date_heart_rate = (TextView) row.findViewById(R.id.date_heart_rate);
                viewHolder.heart_rate = (TextView) row.findViewById(R.id.heart_rate);
                row.setTag(viewHolder);

                viewHolder.date_heart_rate.setText(stat[0]);
                viewHolder.heart_rate.setText(stat[1]);
            }

        } else {
            viewHolder = (ItemViewHolder)row.getTag();
        }

        return row;
    }
}