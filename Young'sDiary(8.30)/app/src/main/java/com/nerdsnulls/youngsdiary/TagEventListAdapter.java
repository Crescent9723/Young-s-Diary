package com.nerdsnulls.youngsdiary;

import android.content.Context;
import android.graphics.Color;
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
public class TagEventListAdapter extends BaseAdapter {

    Context ctx;
    int layout;
    ArrayList list;
    LayoutInflater inf;

    public TagEventListAdapter(Context ctx, ArrayList list){
        this.ctx = ctx;
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

        if (list.get(position) instanceof String){
            String item = (String) list.get(position);
            convertView = inf.inflate(R.layout.list_item_section, null);
            convertView.setOnClickListener(null);
            convertView.setOnLongClickListener(null);
            convertView.setLongClickable(false);
            final TextView sectionView =
                    (TextView) convertView.findViewById(R.id.list_item_section_text);
            sectionView.setText(item);
            sectionView.setTextColor(Color.WHITE);
        } else {
            convertView = inf.inflate(R.layout.tag_event_list_row, null);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.titleText);
            TextView txtStartDate = (TextView) convertView.findViewById(R.id.startDateText);
            TextView txtEndDate = (TextView) convertView.findViewById(R.id.endDateText);
            TextView txtDescription = (TextView) convertView.findViewById(R.id.descriptionText);
            TextView txtRepeatData = (TextView) convertView.findViewById(R.id.repeatDataText);
            TextView txtTag = (TextView) convertView.findViewById(R.id.tagText);

            EventData data = (EventData) list.get(position);

            txtTitle.setText(data.getTitle());
            txtStartDate.setText(data.getStartDate().toString());
            txtEndDate.setText(data.getEndDate().toString());
            txtDescription.setText(data.getDescription());
            txtTag.setText(data.getTag());
            String repeatDataText = data.getRepeatData().getRepeatType().getText() + " : ";
            switch (data.getRepeatData().getRepeatType()){
                case YEARLY:
                    repeatDataText += data.getRepeatData().getMonth() + "/" + data.getRepeatData().getDay();
                    break;
                case MONTHLY:
                    repeatDataText += "Chosen days: ";
                    repeatDataText += CommonMethod.getInstance().convertIntArrayToString(data.getRepeatData().getDayList());
                    break;
                case WEEKLY:
                    repeatDataText += "Weekdays: ";
                    repeatDataText += CommonMethod.getInstance().convertIntArrayToString(data.getRepeatData().getWeekdayList());
                    break;
                case DAILY:
                    repeatDataText += "Daily Duration: " + data.getRepeatData().getDuration();
                    break;
            }
            txtRepeatData.setText(repeatDataText);
        }

        return convertView;
    }

}
