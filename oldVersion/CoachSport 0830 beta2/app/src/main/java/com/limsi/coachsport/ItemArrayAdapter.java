package com.limsi.coachsport;

import com.garmin.fit.*;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ItemArrayAdapter extends ArrayAdapter {
    public ArrayList pointlist = new ArrayList();
    public float pointX;
    public float pointY;
    public PointF point;
    public CurveChartView chartView;
    public String fileName;
    public static final long OFFSET = 631065600000l; // Offset between Garmin (FIT) time and Unix time in ms (Dec 31, 1989 - 00:00:00 January 1, 1970).
    private List valueList = new ArrayList();

    static class ItemViewHolder {
        TextView type;
        TextView date;
        TextView time;
        TextView calories;
        TextView distance;
        TextView mood;

        TextView date_label;
        TextView date_heart_rate;
        TextView heart_rate_label;
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
        boolean is_heart_rate = false;
        if (stat[15].equalsIgnoreCase("heart_rate")) {
            is_heart_rate = true;
        }
        if (row == null) {
            if (fileName == "activities") {
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

            } else if (fileName == "heartrate") {
                if (is_heart_rate) {
                    LayoutInflater inflater = (LayoutInflater) this.getContext().
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.item_profile_layout, parent, false);

                    viewHolder = new ItemViewHolder();

                    viewHolder.date_label = (TextView) row.findViewById(R.id.date_label);
                    viewHolder.date_heart_rate = (TextView) row.findViewById(R.id.date_heart_rate);
                    viewHolder.heart_rate_label = (TextView) row.findViewById(R.id.heart_rate_label);
                    viewHolder.heart_rate = (TextView) row.findViewById(R.id.heart_rate);

                    row.setTag(viewHolder);
                    viewHolder.date_label.setText(stat[0]);
                    String temp = getDate(stat[7].replace("\"", ""));
                    viewHolder.date_heart_rate.setText(temp);
                    viewHolder.heart_rate_label.setText(stat[15]);
                    viewHolder.heart_rate.setText(stat[16].replace("\"", "") + " " + stat[17]);
                    Float hour = (Float) Float.parseFloat( temp.replace("\"", "").substring(11,13) );
                    System.out.println("lalala " + temp);
                    System.out.println("lalala " + hour);
                    Float minute = (Float) Float.parseFloat( temp.substring(14,16) );
                    pointX = ( hour*60 + minute ) - 600;
                    pointY = (Float) Float.parseFloat(stat[16].replace("\"", ""));
                    point =  new PointF(pointX,pointY);
                    pointlist.add(point);
                    System.out.println("pointX + pointY: " + pointX + "," + pointY);
                }

            } else {
                viewHolder = (ItemViewHolder) row.getTag();
            }
/*        viewHolder.date_label.setText(stat[0]);
        String temp = getDate(stat[7]);
        viewHolder.date_heart_rate.setText(temp);
        viewHolder.heart_rate_label.setText(stat[15]);
        viewHolder.heart_rate.setText(stat[16] + stat[17]);*/

        }
        return row;
    }

    public static String getDate(String strMilliSeconds) {
        Long longTime = (long) Integer.parseInt(strMilliSeconds);

        // Create a DateFormatter object for displaying date in specified format.
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar cal = Calendar.getInstance(Locale.FRANCE);            // java.util.GregorianCalendar[time=1504076387757,zone=Europe/Amsterdam,ZONE_OFFSET=3600000,
        Calendar cal2 = new GregorianCalendar(1989, 11, 31, -1, -1, 0);
        long diff1989 = cal2.getTime().getTime();                       //Wed Jan 31 00:00:00 GMT+01:00 1990   =>  633740400000
        cal.setTimeInMillis(longTime * 1000 + diff1989);                // set time to **** ms after january 1 1970   off  1415889300
        Date systemCurrentTime = cal.getTime();               //Wed Aug 30 08:59:47 GMT+02:00 2017
        String formatTime = formatter.format(systemCurrentTime);        //30/08/2017 08:59:47.757


       /* System.out.println("longTime: " + longTime);   //  871942560
        System.out.println("cal: " + cal);         // time=1505682960000  2017/08/18
        System.out.println("cal2: " +cal2);         // time=633740400000  1990/0/31
        System.out.println("diff1989: " + diff1989);         // 633740400
        System.out.println(systemCurrentTime);               //Sun Sep 17 23:32:00 GMT+02:00 2017
        System.out.println(formatTime);                      //17/09/2017 11:32:00*/

        return formatTime;
    }


}