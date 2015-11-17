package com.nerdsnulls.youngsdiary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private java.util.Calendar month;
    public GregorianCalendar prevMonth;

    public GregorianCalendar prevMonthMaxSet;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeekNumber;
    int maxP;
    int calMaxP;
    int monthLength;
    String itemValue, currentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<>();
        Locale.setDefault( Locale.US );
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        currentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int width, height;
        width = (int) (metrics.widthPixels * 0.93) / 7;
        height = (int) (metrics.heightPixels * 0.6 * 0.75) / (maxWeekNumber);
        v.setLayoutParams(new GridView.LayoutParams(width, height));
        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = dayString.get(position).split("-");
        int gridValue = Integer.parseInt(separatedTime[2]);
        if ((gridValue > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.LTGRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((gridValue < 7) && (position > 28)) {
            dayView.setTextColor(Color.LTGRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if (position % 7 == 0) {
            dayView.setTextColor(Color.RED);
        } else {
            dayView.setTextColor(Color.BLUE);
        }

        if (dayString.get(position).equals(currentDateString)) {
            if ((gridValue > 1) && (position < firstDay)) {
                setSelected(v, false);
            } else if ((gridValue < 7) && (position > 28)) {
                setSelected(v, false);
            } else {
                setSelected(v, true);
            }
        } else {
            v.setBackgroundResource(R.drawable.grid_border);
        }
        dayView.setText("" + gridValue);

        String date = dayString.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }

        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    public View setSelected(View view, boolean flag) {
        if (flag){
            if (previousView != null) {
                previousView.setBackgroundResource(R.drawable.grid_border);
            }
            previousView = view;
            view.setBackgroundResource(R.drawable.grid_border_selected);
        }
        return view;
    }

    public void refreshDays() {
        items.clear();
        dayString.clear();
        Locale.setDefault( Locale.US );
        prevMonth = (GregorianCalendar) month.clone();
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        maxWeekNumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        monthLength = maxWeekNumber * 7;
        maxP = getMaxP();
        calMaxP = maxP - (firstDay - 1);
        prevMonthMaxSet = (GregorianCalendar) prevMonth.clone();
        prevMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        for (int n = 0; n < monthLength; n++) {
            itemValue = df.format(prevMonthMaxSet.getTime());
            prevMonthMaxSet.add(GregorianCalendar.DATE, 1);
            dayString.add(itemValue);
        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            prevMonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            prevMonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = prevMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}