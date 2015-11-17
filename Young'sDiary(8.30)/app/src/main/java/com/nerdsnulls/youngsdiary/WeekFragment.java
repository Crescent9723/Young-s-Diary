package com.nerdsnulls.youngsdiary;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import data.Authentication;
import data.EventData;
import sqlDatabase.EventSQLiteHandler;

public class WeekFragment extends Fragment {
    TableLayout table;
    TextView title;
    TableRow header;
    LayoutInflater inflater;
    public int weekDaysCount = 0;
    public String[] NextPreWeekday;
    private EventSQLiteHandler handler;

    private FragmentActivity parentActivity;
    private LinearLayout parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_week, container, false);
        setHeaderButtonListener();
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentLayout;
    }

    private void setHeaderButtonListener() {
        RelativeLayout previous = (RelativeLayout) parentLayout.findViewById(R.id.previous);
        RelativeLayout next = (RelativeLayout) parentLayout.findViewById(R.id.next);
        setPrevButtonListener(previous);
        setNextButtonListener(next);
    }

    private void setPrevButtonListener(RelativeLayout previous) {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextPreWeekday = getNewWeekDay(false);
                try {
                    refreshWeek();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNextButtonListener(RelativeLayout next) {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextPreWeekday = getNewWeekDay(true);
                try {
                    refreshWeek();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchEventList(String date, int dayIndex) throws ClassNotFoundException, SQLException, ParseException {
        handler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        String[] dateParse = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateParse[0]), Integer.parseInt(dateParse[1]) - 1, Integer.parseInt(dateParse[2]), 0, 0, 0);

        ArrayList<EventData> list = null;
        try {
            list = handler.selectAllEventByDate(Authentication.getInstance().getUserID(), calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (EventData each : list) {
            if (CommonMethod.getInstance().checkDateWithInRange(each.getStartDate(), each.getEndDate(), calendar.getTime())) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(each.getStartDate());
                cal.set(Calendar.YEAR, Integer.parseInt(dateParse[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(dateParse[1]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParse[2]));
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(each.getEndDate());
                cal2.set(Calendar.YEAR, Integer.parseInt(dateParse[0]));
                cal2.set(Calendar.MONTH, Integer.parseInt(dateParse[1]) - 1);
                cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParse[2]));

                while (cal2.after(cal)) {
                    TableRow tr = (TableRow) table.getChildAt(cal.get(Calendar.HOUR_OF_DAY) + 1);
                    TextView tv = (TextView) tr.getChildAt(dayIndex);
                    if (tv.getText().toString().equals("")){
                        tv.setText("" + each.getTitle());
                    } else {
                        tv.setText(tv.getText().toString() + "\n" + each.getTitle());
                    }
                    cal.add(Calendar.HOUR_OF_DAY, 1);
                }
            }
        }
        handler.close();

    }

    private void init() throws ParseException, SQLException, ClassNotFoundException {
        inflater = parentActivity.getLayoutInflater();
        title = (TextView) parentLayout.findViewById(R.id.title);
        table = (TableLayout) parentLayout.findViewById(R.id.weekTable);
        header = (TableRow) parentLayout.findViewById(R.id.weekHeader);
        for (int i = 0; i < 24; i++) {
            TableRow tr = (TableRow) inflater.inflate(R.layout.week_row, table, false);
            TextView tv = (TextView) tr.getChildAt(0);
            tv.setText(i + ":00");
            tv.setTextColor(Color.BLACK);
            table.addView(tr);
        }
        NextPreWeekday = getWeekDay();
        refreshWeek();
    }

    private void clearTable() {
        for (int i = 1; i < 8; i++) {
            for (int j = 0; j < 24; j++) {
                TableRow tr = (TableRow) table.getChildAt(j);
                TextView tv = (TextView) tr.getChildAt(i);
                tv.setText("");
            }
        }
    }

    private void refreshWeek() throws ParseException, SQLException, ClassNotFoundException {
        title.setText(CommonMethod.convertWeekDaysMonth(NextPreWeekday[0]) + " - " + CommonMethod.convertWeekDaysMonth(NextPreWeekday[6]));
        ((TextView) header.getChildAt(0)).setText(" \n ");
        ((TextView) header.getChildAt(1)).setText(CommonMethod.convertWeekDays(NextPreWeekday[0]) + "\nSun");
        ((TextView) header.getChildAt(2)).setText(CommonMethod.convertWeekDays(NextPreWeekday[1]) + "\nMon");
        ((TextView) header.getChildAt(3)).setText(CommonMethod.convertWeekDays(NextPreWeekday[2]) + "\nTue");
        ((TextView) header.getChildAt(4)).setText(CommonMethod.convertWeekDays(NextPreWeekday[3]) + "\nWed");
        ((TextView) header.getChildAt(5)).setText(CommonMethod.convertWeekDays(NextPreWeekday[4]) + "\nThu");
        ((TextView) header.getChildAt(6)).setText(CommonMethod.convertWeekDays(NextPreWeekday[5]) + "\nFri");
        ((TextView) header.getChildAt(7)).setText(CommonMethod.convertWeekDays(NextPreWeekday[6]) + "\nSat");
        clearTable();
        for (int i = 0; i < 7; i++) {
            fetchEventList(NextPreWeekday[i], i+1);
        }
    }

    public String[] getWeekDay() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    public String[] getNewWeekDay(boolean next) {
        if (next) {
            weekDaysCount++;
        } else {
            weekDaysCount--;
        }
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
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
