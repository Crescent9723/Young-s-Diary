package com.nerdsnulls.youngsdiary;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import data.Authentication;
import data.EventData;
import sqlDatabase.EventSQLiteHandler;

public class MonthFragment extends Fragment {
    private EventSQLiteHandler eventHandler;

    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;
    private RelativeLayout previous, next;
    private TextView title;
    public GregorianCalendar month, itemMonth;

    public CalendarAdapter adapter;
    public ArrayList<String> items;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_month, container, false);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemMonth = (GregorianCalendar) month.clone();
        items = new ArrayList<>();
        adapter = new CalendarAdapter(parentActivity, month);
        GridView gridview = (GridView) parentLayout.findViewById(R.id.calendarGrid);
        gridview.setAdapter(adapter);
        title = (TextView) parentLayout.findViewById(R.id.title);
        previous = (RelativeLayout) parentLayout.findViewById(R.id.previous);
        next = (RelativeLayout) parentLayout.findViewById(R.id.next);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        setPreviousButtonOnClick();
        setNextButtonOnClick();
        setGridViewOnClick(gridview);
        try {
            refreshEventDot();
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parentLayout;
    }

    private void setPreviousButtonOnClick() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                try {
                    refreshCalendar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNextButtonOnClick() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                try {
                    refreshCalendar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setGridViewOnClick(GridView gridview) {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = CalendarAdapter.dayString.get(position);
                String[] separatedTime = selectedGridDate.split("-");
                int gridValue = Integer.parseInt(separatedTime[2]);
                if ((gridValue > 10) && (position < 8)) {
                } else if ((gridValue < 7) && (position > 28)) {
                } else {
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v, true);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[1]) - 1, Integer.parseInt(separatedTime[2]));
                    try {
                        eventHandler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
                        final ArrayList<EventData> list = eventHandler.selectAllEventByDate(Authentication.getInstance().getUserID(), calendar.getTime());
                        ListView listView = (ListView) parentLayout.findViewById(R.id.eventList);
                        MonthEventListAdapter adapter = new MonthEventListAdapter(parentActivity.getApplicationContext(), R.layout.month_event_list_row, list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                EditEventFragment newFragment = new EditEventFragment();
                                Bundle args = new Bundle();
                                args.putInt("eventID", list.get(position).getEventID());
                                args.putString("title", list.get(position).getTitle());
                                args.putString("description", list.get(position).getDescription());
                                args.putString("tag", list.get(position).getTag());
                                args.putSerializable("startDate", list.get(position).getStartDate());
                                args.putSerializable("endDate", list.get(position).getEndDate());
                                args.putInt("icon", list.get(position).getIcon());
                                try {
                                    eventHandler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
                                    args.putSerializable("repeatData", eventHandler.getRepeatDataByEventID(list.get(position).getEventID()));
                                    eventHandler.close();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = parentActivity.getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack so the user can navigate back
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);

                                transaction.commit();
                            }
                        });
                        eventHandler.close();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void refreshEventDot() throws ParseException {
        eventHandler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        ArrayList<EventData> list = eventHandler.selectAllEventByUser(Authentication.getInstance().getUserID());
        Set<String> dateSet = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        for (EventData each : list){
            cal.setTime(each.getStartDate());
            Date endDate = each.getEndDate();
            while (endDate.after(cal.getTime())){
                dateSet.add(dateFormat.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        items.addAll(dateSet);
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        eventHandler.close();
    }

    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() throws ParseException {
        adapter.refreshDays();
        refreshEventDot();
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    private void init() throws ParseException {
        eventHandler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        final ArrayList<EventData> list = eventHandler.selectAllEventByDate(Authentication.getInstance().getUserID(), Calendar.getInstance().getTime());
        ListView listView = (ListView) parentLayout.findViewById(R.id.eventList);
        MonthEventListAdapter adapter = new MonthEventListAdapter(parentActivity.getApplicationContext(), R.layout.month_event_list_row, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditEventFragment newFragment = new EditEventFragment();
                Bundle args = new Bundle();
                args.putInt("eventID", list.get(position).getEventID());
                args.putString("title", list.get(position).getTitle());
                args.putString("description", list.get(position).getDescription());
                args.putString("tag", list.get(position).getTag());
                args.putSerializable("startDate", list.get(position).getStartDate());
                args.putSerializable("endDate", list.get(position).getEndDate());
                args.putInt("icon", list.get(position).getIcon());
                newFragment.setArguments(args);

                FragmentTransaction transaction = parentActivity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        eventHandler.close();
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
