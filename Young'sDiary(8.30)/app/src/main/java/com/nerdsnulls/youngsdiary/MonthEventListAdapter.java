package com.nerdsnulls.youngsdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import data.EventData;

/**
 * Created by Justin on 8/18/2015.
 */
public class MonthEventListAdapter extends BaseAdapter {

    Context ctx;
    int layout;
    ArrayList<EventData> list;
    LayoutInflater inf;

    public MonthEventListAdapter(Context ctx, int layout, ArrayList<EventData> list){
        this.ctx = ctx;
        this.layout = layout;
        this.list = list;
        inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inf.inflate(layout, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.titleText);
        TextView txtStartDate = (TextView) convertView.findViewById(R.id.startDateText);
        TextView txtEndDate = (TextView) convertView.findViewById(R.id.endDateText);
        TextView txtDescription = (TextView) convertView.findViewById(R.id.descriptionText);
        TextView txtTag = (TextView) convertView.findViewById(R.id.tagText);

        EventData data = list.get(position);

        txtTitle.setText(data.getTitle());
        txtStartDate.setText(data.getStartDate().toString());
        txtEndDate.setText(data.getEndDate().toString());
        txtDescription.setText(data.getDescription());
        txtTag.setText(data.getTag());
        return convertView;
    }

}
