package com.nerdsnulls.youngsdiary;

import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Calendar;

import data.Authentication;

public class ContainerActivity extends FragmentActivity {
    private FrameLayout fragContainer;
    private ImageView addButton;
    private Button dayButton;
    private Button weekButton;
    private Button monthButton;
    private Button yearButton;
    private Button tagButton;
    private Button settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        setButtonListListener();
        Authentication.getInstance().setUserID("yychoi");
        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);
        if (fragContainer != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MainFragment firstFragment = new MainFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    private void setButtonListListener() {
        yearButton = (Button) findViewById(R.id.yearlyButton);
        monthButton = (Button) findViewById(R.id.monthlyButton);
        weekButton = (Button) findViewById(R.id.weeklyButton);
        dayButton = (Button) findViewById(R.id.dailyButton);
        tagButton = (Button) findViewById(R.id.tagButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        addButton = (ImageView) findViewById(R.id.addEventButton);
        addButton.setOnTouchListener(new View.OnTouchListener() {
            private static final int MAX_CLICK_DURATION = 200;
            private long startClickTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            ImageView view = (ImageView) v;
                            view.getDrawable().clearColorFilter();
                            view.invalidate();
                            disableSelectedButton(addButton);
                            AddEventFragment newFragment = new AddEventFragment();
                            setFragmentTransaction(newFragment);
                        }
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
        yearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(yearButton);
                YearFragment newFragment = new YearFragment();
                setFragmentTransaction(newFragment);
            }
        });
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(monthButton);
                MonthFragment newFragment = new MonthFragment();
                setFragmentTransaction(newFragment);
            }
        });
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(weekButton);
                WeekFragment newFragment = new WeekFragment();
                setFragmentTransaction(newFragment);
            }
        });
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(dayButton);
                DayFragment newFragment = new DayFragment();
                setFragmentTransaction(newFragment);
            }
        });
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(tagButton);
                TagFragment newFragment = new TagFragment();
                setFragmentTransaction(newFragment);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectedButton(settingsButton);
                SettingsFragment newFragment = new SettingsFragment();
                setFragmentTransaction(newFragment);
            }
        });
    }

    private void setFragmentTransaction(Fragment newFragment) {
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void disableSelectedButton(Object button) {
        yearButton.setEnabled(true);
        monthButton.setEnabled(true);
        weekButton.setEnabled(true);
        dayButton.setEnabled(true);
        tagButton.setEnabled(true);
        settingsButton.setEnabled(true);
        addButton.setImageResource(R.drawable.ic_add_icon);
        addButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (button != null){
            if (button == addButton){
                addButton.setImageResource(R.drawable.ic_add_disabled_icon);
                addButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                ((Button) button).setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
            Fragment frag = fm.findFragmentById(R.id.fragment_container);
            if (frag instanceof YearFragment){
                disableSelectedButton(yearButton);
            } else if (frag instanceof MonthFragment){
                disableSelectedButton(monthButton);
            } else if (frag instanceof WeekFragment){
                disableSelectedButton(weekButton);
            } else if (frag instanceof DayFragment){
                disableSelectedButton(dayButton);
            } else if (frag instanceof TagFragment){
                disableSelectedButton(tagButton);
            } else if (frag instanceof SettingsFragment){
                disableSelectedButton(settingsButton);
            } else if (frag instanceof AddEventFragment || frag instanceof EditEventFragment){
                disableSelectedButton(addButton);
            } else {
                disableSelectedButton(null);
            }
        } else {
            super.onBackPressed();
        }
    }
}
