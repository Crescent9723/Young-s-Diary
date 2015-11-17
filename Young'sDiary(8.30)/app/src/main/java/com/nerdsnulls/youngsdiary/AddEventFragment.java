package com.nerdsnulls.youngsdiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import data.EventIconData;
import data.RepeatData;
import data.RepeatType;
import sqlDatabase.EventSQLiteHandler;


/**
 * Created by Justin on 8/21/2015.
 */
public class AddEventFragment extends Fragment {
    private RepeatData repeatData;
    private Button cancelButton;
    private Button okButton;
    private EditText eventNameEdit;
    private EditText eventDescriptionEdit;
    private EditText eventTagEdit;
    private Spinner iconSelectSpinner;
    private Button setDateButton;
    private TextView startDateText;
    private TextView endDateText;
    private TextView repeatDetailText;
    private Button setRepeatButton;
    private EventSQLiteHandler handler;
    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;
    private String eventName;
    private String eventDescription;
    private String eventTag;
    private int eventIconIndex;

    private Integer[] imageIconDatabase = { R.drawable.ic_birthday_icon,
            R.drawable.ic_book_icon, R.drawable.ic_call_icon, R.drawable.ic_coffee_icon,
            R.drawable.ic_food_icon, R.drawable.ic_meeting_icon};
    private String[] imageNameDatabase = { "Birthday", "Book", "Call", "Coffee",
            "Food", "Meeting"};
    private ArrayList<EventIconData> iconList = new ArrayList<EventIconData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_add_event, container, false);
        if (parentActivity.getIntent().hasExtra("repeatData")){
            Bundle bundle = parentActivity.getIntent().getExtras();
            repeatData = (RepeatData) bundle.getSerializable("repeatData");
            parentActivity.getIntent().removeExtra("repeatData");
        }
        okButton = (Button) parentLayout.findViewById(R.id.okButton);
        cancelButton = (Button) parentLayout.findViewById(R.id.cancelButton);
        eventNameEdit = (EditText) parentLayout.findViewById(R.id.eventNameEdit);
        eventDescriptionEdit = (EditText) parentLayout.findViewById(R.id.eventDescriptionEdit);
        eventTagEdit = (EditText) parentLayout.findViewById(R.id.eventTagEdit);
        iconSelectSpinner = (Spinner) parentLayout.findViewById(R.id.iconSelectSpinner);
        setDateButton = (Button) parentLayout.findViewById(R.id.setDateButton);
        setRepeatButton = (Button) parentLayout.findViewById(R.id.setRepeatButton);
        startDateText = (TextView) parentLayout.findViewById(R.id.startDateText);
        endDateText = (TextView) parentLayout.findViewById(R.id.endDateText);
        repeatDetailText = (TextView) parentLayout.findViewById(R.id.repeatDetailText);
        if (repeatData != null){
            if (repeatData.getRepeatType() == RepeatType.NONE){
                initStartAndEndDate();
            } else {
                initStartAndEndRepeatDate();
            }
        }
        initValues();
        setOKButtonListener();
        setCancelButtonListener();
        setIconSelectSpinnerAdapter();
        setDateButtonListener();
        setRepeatButtonListener();
        return parentLayout;
    }

    private void initValues() {
        eventNameEdit.setText(eventName);
        eventDescriptionEdit.setText(eventDescription);
        eventTagEdit.setText(eventTag);
        iconSelectSpinner.setSelection(eventIconIndex);
    }

    private void initStartAndEndRepeatDate() {
        startDateText.setText("Repeat Start Date: " + CommonMethod.getInstance().getDateToString(repeatData.getStartDate()));
        endDateText.setText("Repeat End Date: " + CommonMethod.getInstance().getDateToString(repeatData.getEndDate()));
        repeatDetailText.setText(parentActivity.getIntent().getStringExtra("repeatDetail"));
    }

    private void initStartAndEndDate() {
        startDateText.setText("Start Date: " + CommonMethod.getInstance().getDateToString(repeatData.getStartDate()));
        endDateText.setText("End Date: " + CommonMethod.getInstance().getDateToString(repeatData.getEndDate()));
    }

    private void setDateButtonListener() {
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInstace();
                SetByDateAndTimeFragment newFragment = new SetByDateAndTimeFragment();
                Bundle args = new Bundle();
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
    }

    private void saveInstace() {
        eventName = eventNameEdit.getText().toString();
        eventDescription = eventDescriptionEdit.getText().toString();
        eventTag = eventTagEdit.getText().toString();
        eventIconIndex = iconSelectSpinner.getSelectedItemPosition();
    }

    private void setRepeatButtonListener() {
        setRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInstace();
                SetByRepeatFragment newFragment = new SetByRepeatFragment();
                Bundle args = new Bundle();
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
    }

    private void setIconSelectSpinnerAdapter() {
        for (int i = 0 ; i < 6 ; i++){
            iconList.add(new EventIconData(imageNameDatabase[i], imageIconDatabase[i]));
        }
        IconSpinnerAdapter adapter = new IconSpinnerAdapter(parentActivity, R.layout.icon_spinner_row, iconList, parentActivity.getResources());
        iconSelectSpinner.setAdapter(adapter);
    }

    private void setCancelButtonListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onBackPressed();
            }
        });
    }

    private void setOKButtonListener(){
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatData == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    builder.setTitle("Error!!");
                    TextView errorText = new TextView(parentActivity);
                    errorText.setText("Date and time are not set!");
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
                handler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
                handler.insert("yychoi", eventNameEdit.getText().toString(), repeatData.getStartDate(), repeatData.getEndDate(),
                        eventDescriptionEdit.getText().toString(), eventTagEdit.getText().toString().toLowerCase(),
                        ((EventIconData) iconSelectSpinner.getSelectedItem()).getResourceID(), repeatData.getRepeatType(), repeatData.getDuration(),
                        repeatData.getWeekdayList(), repeatData.getDayList(), repeatData.getMonth(), repeatData.getDay());

                parentActivity.findViewById(R.id.addEventButton).setEnabled(true);
                parentActivity.onBackPressed();
            }
        });
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
