package com.nerdsnulls.youngsdiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import data.Authentication;
import data.EventData;
import data.NoteData;
import data.RepeatData;
import data.TodoData;
import sqlDatabase.EventSQLiteHandler;
import sqlDatabase.NoteSQLiteHandler;
import sqlDatabase.TodoSQLiteHandler;

public class DayFragment extends Fragment {
    Calendar currentDay;
    TableLayout dailyTable;
    TableLayout todoTable;
    EditText noteEditText;
    TextView title;
    Button addTodoButton;
    private EventSQLiteHandler eventHandler;
    private NoteSQLiteHandler noteHandler;
    private TodoSQLiteHandler todoHandler;
    private NoteData currentNote;

    private FragmentActivity parentActivity;
    private LinearLayout parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_day, container, false);

        currentDay = Calendar.getInstance();
        dailyTable = (TableLayout) parentLayout.findViewById(R.id.dailyTable);
        todoTable = (TableLayout) parentLayout.findViewById(R.id.todoTable);
        noteEditText = (EditText) parentLayout.findViewById(R.id.noteEditText);
        addTodoButton = (Button) parentLayout.findViewById(R.id.addTodoButton);
        addTodoButtonListener();
        initEditText(noteEditText);
        initDailyTable();
        setHeaderButtonListener();
        setNoteEditTextListener();
        try {
            refresh();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parentLayout;
    }

    private void addTodoButtonListener() {
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Add TODO");
                final EditText input = new EditText(parentActivity);
                initEditText(input);
                input.setFocusableInTouchMode(true);
                builder.setView(input);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoHandler = TodoSQLiteHandler.open(parentActivity.getApplicationContext());
                        todoHandler.insert(input.getText().toString(), currentDay.getTime(), Authentication.getInstance().getUserID());
                        refreshTodo();
                        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        return;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        return;
                    }
                });
                builder.show();
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
    }

    private void initEditText(EditText edit) {
        edit.setGravity(Gravity.LEFT | Gravity.TOP);
        edit.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        edit.setTextColor(Color.BLACK);
        edit.setSingleLine(false);
        edit.setFocusableInTouchMode(false);
        edit.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        edit.setMaxLines(Integer.MAX_VALUE);
        edit.setHorizontallyScrolling(false);
    }

    private void setNoteEditTextListener() {
        noteEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setTitle("Update Note");
                final EditText input = new EditText(parentActivity);
                initEditText(input);
                input.setFocusableInTouchMode(true);
                input.setText(currentNote.getText());
                builder.setView(input);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteHandler = NoteSQLiteHandler.open(parentActivity.getApplicationContext());
                        noteHandler.update(currentNote.getNoteID(), input.getText().toString(), currentDay.getTime(), Authentication.getInstance().getUserID());
                        try {
                            refreshNote();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        return;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        return;
                    }
                });
                builder.show();
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
    }

    private void initDailyTable() {
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        for (int i = 0; i < 24; i++) {
            TableRow tr = (TableRow) inflater.inflate(R.layout.daily_row, dailyTable, false);
            TextView tv = (TextView) tr.findViewById(R.id.timeText);
            tv.setText(i + ":00");
            dailyTable.addView(tr);
        }
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
                currentDay.add(Calendar.DAY_OF_MONTH, -1);
                try {
                    refresh();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNextButtonListener(RelativeLayout next) {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay.add(Calendar.DAY_OF_MONTH, 1);
                try {
                    refresh();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refresh() throws ParseException {
        refreshDayTable();
        refreshNote();
        refreshTodo();
    }

    private void refreshDayTable() {
        eventHandler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        title = (TextView) parentLayout.findViewById(R.id.title);
        title.setText(currentDay.get(Calendar.YEAR) + "/" + (currentDay.get(Calendar.MONTH) + 1) + "/" + currentDay.get(Calendar.DAY_OF_MONTH));

        for (int i = 0; i < 24; i++) {
            TableRow tr = (TableRow) dailyTable.getChildAt(i);
            LinearLayout ll = (LinearLayout) tr.getChildAt(1);
            ll.removeAllViews();
        }

        try {
            final ArrayList<EventData> list = eventHandler.selectAllEventByDate(Authentication.getInstance().getUserID(), currentDay.getTime());
            for (final EventData each : list){
                final int eventID = each.getEventID();
                final String title = each.getTitle();
                final String description = each.getDescription();
                final String tag = each.getTag();
                final Date start = each.getStartDate();
                final Date end = each.getEndDate();
                final int icon = each.getIcon();
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(start);
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(end);
                startDate.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH));
                endDate.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH));
                int hour = startDate.get(Calendar.HOUR_OF_DAY);
                while (endDate.after(startDate)) {
                    TableRow tr = (TableRow) dailyTable.getChildAt(hour);
                    LinearLayout ll = (LinearLayout) tr.getChildAt(1);
                    TextView tv = new TextView(parentActivity);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(lp);
                    tv.setText(Html.fromHtml("<big><i>" + title + "</i></big>" + " : " + description));
                    tv.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.day_text_bg));
                    ll.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            EditEventFragment newFragment = new EditEventFragment();
                            Bundle args = new Bundle();
                            args.putInt("eventID", eventID);
                            args.putString("title", title);
                            args.putString("description", description);
                            args.putString("tag", tag);
                            args.putSerializable("startDate", start);
                            args.putSerializable("endDate", end);
                            args.putInt("icon", icon);
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

                    startDate.add(Calendar.HOUR_OF_DAY, 1);
                    hour++;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        eventHandler.close();
    }

    private void refreshNote() throws ParseException {
        noteHandler = NoteSQLiteHandler.open(parentActivity.getApplicationContext());
        currentNote = noteHandler.selectNoteByDate(Authentication.getInstance().getUserID(), currentDay.getTime());
        noteEditText.setText(currentNote.getText());
        noteHandler.close();
    }

    private void refreshTodo() {
        todoHandler = TodoSQLiteHandler.open(parentActivity.getApplicationContext());
        try {
            todoTable.removeAllViews();
            final ArrayList<TodoData> list = todoHandler.selectTodoByDate(Authentication.getInstance().getUserID(), currentDay.getTime());
            for (TodoData each : list){
                final int todoID = each.getTodoID();
                TextView tv = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.todo_row, todoTable, false);
                tv.setText(each.getText());
                tv.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.day_text_bg));
                todoTable.addView(tv);
                tv.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                        builder.setTitle("Delete TODO");
                        TextView deleteText = new TextView(parentActivity);
                        deleteText.setText("Do you really want to delete?");
                        builder.setView(deleteText);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                todoHandler.delete(todoID);
                                refreshTodo();
                                return;
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        builder.show();
                        return false;
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todoHandler.close();
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
