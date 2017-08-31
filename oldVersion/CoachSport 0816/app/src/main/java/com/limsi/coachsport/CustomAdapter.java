package com.limsi.coachsport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

    private int[] icons;

    public CustomAdapter(Context context, String[] details,int[] pics) {
        super(context, R.layout.custom_row,details);
        icons = pics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        String userDetails = getItem(position);

        TextView detailsText = (TextView) customView.findViewById(R.id.UserDetails);

        detailsText.setText(userDetails);

        return customView;
    }
}
