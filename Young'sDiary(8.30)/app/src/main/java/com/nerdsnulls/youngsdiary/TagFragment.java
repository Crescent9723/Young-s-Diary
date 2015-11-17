package com.nerdsnulls.youngsdiary;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

import data.Authentication;
import data.EventData;
import sqlDatabase.EventSQLiteHandler;

public class TagFragment extends Fragment {
    ListView eventList;
    EditText tagEdit;
    private EventSQLiteHandler handler;

    private LinearLayout parentLayout;
    private FragmentActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = super.getActivity();
        parentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_tag, container, false);

        eventList = (ListView) parentLayout.findViewById(R.id.eventList);
        tagEdit = (EditText) parentLayout.findViewById(R.id.tagEdit);
        DisplayMetrics metrics = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        eventList.getLayoutParams().height =  (metrics.heightPixels * 875 / 1000);
        tagEdit.getLayoutParams().height =  (metrics.heightPixels * 7 / 100);
        refreshListDefault();
        setTagEditListener();
        return parentLayout;
    }

    private void setTagEditListener() {
        tagEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    refreshListDefault();
                } else {
                    refreshListBySearch();
                }
            }
        });
    }

    private void refreshListBySearch(){
        handler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        try {
            final ArrayList list = handler.selectAllEventByTag(Authentication.getInstance().getUserID(), tagEdit.getText().toString().toLowerCase());
            String compare = "";
            for (int i = 0 ; i < list.size() ; i++){
                if (!((EventData)list.get(i)).getTag().equals(compare)){
                    compare = ((EventData)list.get(i)).getTag();
                    list.add(i, compare);
                }
            }
            TagEventListAdapter adapter = new TagEventListAdapter(parentActivity.getApplicationContext(), list);
            eventList.setAdapter(adapter);
            setEventListOnClick(list);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        handler.close();
    }

    private void refreshListDefault(){
        handler = EventSQLiteHandler.open(parentActivity.getApplicationContext());
        try {
            final ArrayList list = handler.selectAllEventByUserOrderByTag(Authentication.getInstance().getUserID());
            String compare = "";
            for (int i = 0 ; i < list.size() ; i++){
                if (!((EventData)list.get(i)).getTag().equals(compare)){
                    compare = ((EventData)list.get(i)).getTag();
                    list.add(i, compare);
                }
            }
            TagEventListAdapter adapter = new TagEventListAdapter(parentActivity.getApplicationContext(), list);
            eventList.setAdapter(adapter);
            setEventListOnClick(list);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        handler.close();
    }

    private void setEventListOnClick(final ArrayList<EventData> list) {
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
