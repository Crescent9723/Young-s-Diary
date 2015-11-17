package com.nerdsnulls.youngsdiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import data.RepeatData;
import data.RepeatType;


public class SetByDateAndTimeFragment extends Fragment {
    Calendar myCalendar = Calendar.getInstance();

    private RepeatData repeatData;
    private Button cancelButton;
    private Button okButton;
    private EditText eventStartDateEdit;
    private EditText eventEndDateEdit;
    private EditText eventStartTimeEdit;
    private EditText eventEndTimeEdit;
    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_set_by_date_and_time, container, false);
        okButton = (Button) parentLayout.findViewById(R.id.okButton);
        cancelButton = (Button) parentLayout.findViewById(R.id.cancelButton);
        eventStartDateEdit = (EditText) parentLayout.findViewById(R.id.startDateEdit);
        eventEndDateEdit = (EditText) parentLayout.findViewById(R.id.endDateEdit);
        eventStartTimeEdit = (EditText) parentLayout.findViewById(R.id.startTimeEdit);
        eventEndTimeEdit = (EditText) parentLayout.findViewById(R.id.endTimeEdit);
        setStartDateListener();
        setEndDateListener();
        setStartTimeListener();
        setEndTimeListener();
        setOKButtonListener();
        setCancelButtonListener();
        return parentLayout;
    }

    private void setCancelButtonListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setOKButtonListener(){
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForNullAndEmpty(eventStartDateEdit) || checkForNullAndEmpty(eventEndDateEdit)
                        || checkForNullAndEmpty(eventStartTimeEdit) || checkForNullAndEmpty(eventEndTimeEdit)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    builder.setTitle("Error!!");
                    TextView errorText = new TextView(parentActivity);
                    errorText.setText("You need to choose date and time!");
                    builder.setView(errorText);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                    return;
                }
                String startDate = eventStartDateEdit.getText().toString() + "-" + eventStartTimeEdit.getText().toString();
                String endDate = eventEndDateEdit.getText().toString() + "-" + eventEndTimeEdit.getText().toString();
                String[] startDateToken = startDate.split("-|:");
                String[] endDateToken = endDate.split("-|:");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(startDateToken[0]), Integer.parseInt(startDateToken[1]) - 1,
                        Integer.parseInt(startDateToken[2]), Integer.parseInt(startDateToken[3]),
                        Integer.parseInt(startDateToken[4]), 00);
                Date startDateObject = cal.getTime();
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Integer.parseInt(endDateToken[0]), Integer.parseInt(endDateToken[1]) - 1,
                        Integer.parseInt(endDateToken[2]), Integer.parseInt(endDateToken[3]),
                        Integer.parseInt(endDateToken[4]), 00);
                Date endDateObject = cal2.getTime();
                if (startDateObject.after(endDateObject)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    builder.setTitle("Error!!");
                    TextView errorText = new TextView(parentActivity);
                    errorText.setText("Start Date cannot be faster than End Date");
                    builder.setView(errorText);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                    return;
                } else if (cal.get(Calendar.HOUR_OF_DAY) > cal2.get(Calendar.HOUR_OF_DAY)
                        || (cal.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
                        && cal.get(Calendar.MINUTE) > cal2.get(Calendar.MINUTE))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    builder.setTitle("Error!!");
                    TextView errorText = new TextView(parentActivity);
                    errorText.setText("Start Time cannot be faster than End Time");
                    builder.setView(errorText);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                    return;
                }
                repeatData = new RepeatData.Builder(startDateObject, endDateObject, RepeatType.NONE).build();

                parentActivity.getIntent().putExtra("repeatData", repeatData);
                parentActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setStartDateListener(){
        eventStartDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(parentActivity, startDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dlg.show();
            }
        });
    }

    private void setEndDateListener(){
        eventEndDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(parentActivity, endDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dlg.show();
            }
        });
    }
    private void setStartTimeListener(){
        eventStartTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dlg = new TimePickerDialog(parentActivity, startTimePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                dlg.show();
            }
        });
    }
    private void setEndTimeListener(){
        eventEndTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dlg = new TimePickerDialog(parentActivity, endTimePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                dlg.show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel(eventStartDateEdit);
        }

    };

    DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel(eventEndDateEdit);
        }

    };

    TimePickerDialog.OnTimeSetListener startTimePicker = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel(eventStartTimeEdit);
        }
    };
    TimePickerDialog.OnTimeSetListener endTimePicker = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel(eventEndTimeEdit);
        }
    };

    private void updateTimeLabel(EditText editText) {

        SimpleDateFormat originalFormat = new SimpleDateFormat(
                "H-m", Locale.ENGLISH);

        editText.setText(originalFormat.format(myCalendar.getTime()));
    }

    private void updateDateLabel(EditText editText) {

        SimpleDateFormat originalFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.ENGLISH);

        editText.setText(originalFormat.format(myCalendar.getTime()));
    }

    private boolean checkForNullAndEmpty(EditText edit) {
        return edit.getText().toString() == null || edit.getText().toString().equals("");
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
