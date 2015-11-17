package com.nerdsnulls.youngsdiary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import data.EventIconData;

/**
 * Created by Justin on 9/7/2015.
 */
public class IconSpinnerAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private ArrayList data;
    public Resources res;
    EventIconData tempValues = null;
    LayoutInflater inflater;
    public IconSpinnerAdapter(FragmentActivity activitySpinner, int textViewResourceId, ArrayList objects, Resources resLocal){
        super(activitySpinner, textViewResourceId, objects);
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getPosition(String item) {
        return super.getPosition(item);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.icon_spinner_row, parent, false);
        tempValues = (EventIconData) data.get(position);
        TextView label = (TextView)row.findViewById(R.id.spinner_name);
        ImageView icon = (ImageView)row.findViewById(R.id.spinner_image);
        label.setText(tempValues.getName());
        icon.setImageResource(tempValues.getResourceID());
        return row;
    }
}
