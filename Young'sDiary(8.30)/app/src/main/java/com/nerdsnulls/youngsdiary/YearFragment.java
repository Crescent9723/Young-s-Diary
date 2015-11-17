package com.nerdsnulls.youngsdiary;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.SQLException;

import data.Authentication;
import data.EventData;
import sqlDatabase.EventSQLiteHandler;

// TODO: 9/10/2015 Handle Year change 
public class YearFragment extends Fragment {
    TableLayout table;
    LinearLayout dayList;
    private EventSQLiteHandler handler;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_year, container, false);

        init();
        try {
            fetchEventList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parentLayout;
    }

    private void fetchEventList() throws ClassNotFoundException, SQLException, ParseException {
        handler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        ArrayList<EventData> list = handler.selectAllEventByUser(Authentication.getInstance().getUserID());

        for (EventData each : list){
            startDate.setTime(each.getStartDate());
            endDate.setTime(each.getEndDate());
            int month = startDate.get(Calendar.MONTH);
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            while (endDate.after(startDate)) {
                TableRow tr = (TableRow) table.getChildAt(day);
                LinearLayout ll = (LinearLayout) tr.getChildAt(month);
                ImageView view = new ImageView(parentActivity);
                view.setImageResource(each.getIcon());
                ll.addView(view);
                startDate.add(Calendar.DAY_OF_MONTH, 1);
                month = startDate.get(Calendar.MONTH);
                day = startDate.get(Calendar.DAY_OF_MONTH);
            }
        }
        handler.close();
    }

    private void init() {
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        table = (TableLayout) parentLayout.findViewById(R.id.yearTable);
        dayList = (LinearLayout) parentLayout.findViewById(R.id.dayLinearList);
        DisplayMetrics metrics = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width, height;
        if(parentActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            width = (int) (metrics.widthPixels * 0.93) / 7;
            height = metrics.heightPixels / 16;
        } else {
            width = (int) (metrics.widthPixels * 0.93) / 4;
            height = metrics.heightPixels / 32;
        }
        TextView dayHeader = (TextView) inflater.inflate(R.layout.day_header, dayList, false);
        dayHeader.getLayoutParams().height = height;
        dayHeader.getLayoutParams().width = width / 3;
        dayList.addView(dayHeader);
        for (int i = 1; i < 32; i++) {
            TextView text = (TextView) inflater.inflate(R.layout.day_list, table, false);
            text.getLayoutParams().height = height;
            text.getLayoutParams().width = width / 3;
            text.setText("" + i);
            dayList.addView(text);
        }
        dayList.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT));
        TableRow header = (TableRow) inflater.inflate(R.layout.year_header, table, false);
        for (int i = 0; i < 12 ; i++){
            header.getChildAt(i).getLayoutParams().height = height;
            header.getChildAt(i).getLayoutParams().width = width;
        }
        table.addView(header);
        for (int i = 1; i < 32; i++) {
            TableRow tr = (TableRow) inflater.inflate(R.layout.year_row, table, false);
            for (int j = 0; j < 12; j++){
                LinearLayout ll = (LinearLayout) tr.getChildAt(j);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(width, height);
                ll.setLayoutParams(lp);
            }
            table.addView(tr);
        }
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        // HW layer support only exists on API 11+
        if (Build.VERSION.SDK_INT >= 11) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }

            if (animation != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        getView().setLayerType(View.LAYER_TYPE_NONE, null);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    // ...other AnimationListener methods go here...
                });
            }
        }

        return animation;
    }
}
