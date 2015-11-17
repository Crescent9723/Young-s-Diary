package com.nerdsnulls.youngsdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import data.Authentication;
import data.RepeatData;
import data.RepeatType;
import sqlDatabase.NoteSQLiteHandler;


public class SetRepeatTypeFragment extends Fragment {
    Calendar myCalendar = Calendar.getInstance();

    private RepeatData repeatData;
    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;
    private Button setYearlyRepeatButton;
    private Button setMonthlyRepeatButton;
    private Button setWeeklyRepeatButton;
    private Button setDailyRepeatButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_set_repeat_type, container, false);
        setYearlyRepeatButton = (Button) parentLayout.findViewById(R.id.setYearlyRepeatButton);
        setMonthlyRepeatButton = (Button) parentLayout.findViewById(R.id.setMonthlyRepeatButton);
        setWeeklyRepeatButton = (Button) parentLayout.findViewById(R.id.setWeeklyRepeatButton);
        setDailyRepeatButton = (Button) parentLayout.findViewById(R.id.setDailyRepeatButton);
        setYearlyRepeatButtonListener();
        setMonthlyRepeatButtonListener();
        setWeeklyRepeatButtonListener();
        setDailyRepeatButtonListener();
        return parentLayout;
    }

    private void setDailyRepeatButtonListener(){
        setDailyRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Choose Duration");
                final EditText input = new EditText(parentActivity);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int duration = Integer.parseInt(input.getText().toString());
                        repeatData = new RepeatData.Builder(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), RepeatType.DAILY).duration(duration).build();
                        parentActivity.getIntent().putExtra("repeatData", repeatData);
                        parentActivity.getSupportFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }

    private void setWeeklyRepeatButtonListener(){
        setWeeklyRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Select Weekdays");
                final ArrayList<Integer> selectedDayList = new ArrayList<>();
                final ListView input = new ListView(parentActivity);
                input.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                final String[] dayList = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                input.setAdapter(new ArrayAdapter<String>(parentActivity, android.R.layout.simple_list_item_multiple_choice, dayList));
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray sp = input.getCheckedItemPositions();
                        selectedDayList.clear();
                        for (int i = 0; i < input.getAdapter().getCount(); i++) {
                            if (sp.get(i)) {
                                selectedDayList.add(i);
                            }
                        }
                        int[] selectedDayArray = new int[selectedDayList.size()];
                        Iterator<Integer> iterator = selectedDayList.iterator();
                        for (int i = 0; i < selectedDayArray.length; i++) {
                            selectedDayArray[i] = iterator.next().intValue();
                        }
                        repeatData = new RepeatData.Builder(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), RepeatType.WEEKLY).weekdayList(selectedDayArray).build();
                        parentActivity.getIntent().putExtra("repeatData", repeatData);
                        parentActivity.getSupportFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }

    private void setMonthlyRepeatButtonListener(){
        setMonthlyRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Select Days");
                final ArrayList<Integer> selectedDayList = new ArrayList<>();
                final ListView input = new ListView(parentActivity);
                input.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                final ArrayList<Integer> dayList = new ArrayList<Integer>();
                for (int i = 1 ; i < 32 ; i++){
                    dayList.add(i);
                }
                input.setAdapter(new ArrayAdapter<Integer>(parentActivity, android.R.layout.simple_list_item_multiple_choice, dayList));
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray sp = input.getCheckedItemPositions();
                        selectedDayList.clear();
                        for (int i = 0; i < input.getAdapter().getCount(); i++) {
                            if (sp.get(i)) {
                                selectedDayList.add(i+1);
                            }
                        }
                        int[] selectedDayArray = new int[selectedDayList.size()];
                        Iterator<Integer> iterator = selectedDayList.iterator();
                        for (int i = 0; i < selectedDayArray.length; i++)
                        {
                            selectedDayArray[i] = iterator.next().intValue();
                        }
                        repeatData = new RepeatData.Builder(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), RepeatType.MONTHLY).dayList(selectedDayArray).build();
                        parentActivity.getIntent().putExtra("repeatData", repeatData);
                        parentActivity.getSupportFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }

    private void setYearlyRepeatButtonListener(){
        setYearlyRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(parentActivity, yearDayPicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState)
                    {
                        super.onCreate(savedInstanceState);
                        int year = getContext().getResources()
                                .getIdentifier("android:id/year", null, null);
                        if(year != 0){
                            View yearPicker = findViewById(year);
                            if(yearPicker != null){
                                yearPicker.setVisibility(View.GONE);
                            }
                        }
                    }
                };
                dlg.show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener yearDayPicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            repeatData = new RepeatData.Builder(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), RepeatType.YEARLY).day(dayOfMonth).month(monthOfYear).build();
            parentActivity.getIntent().putExtra("repeatData", repeatData);
            parentActivity.getSupportFragmentManager().popBackStack();
        }

    };

}
