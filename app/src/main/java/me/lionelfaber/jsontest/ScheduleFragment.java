package me.lionelfaber.jsontest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by lionel on 15/10/17.
 */

public class ScheduleFragment extends Fragment implements OnDateSelectedListener {


        MaterialCalendarView widget;
        DatabaseHandler db;
        String dateString;
        ExpandableListView elv;



    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            // Defines the xml file for the fragment
            View v = inflater.inflate(R.layout.schedule_layout, null);
            db  = new DatabaseHandler(getContext());
            widget = (MaterialCalendarView)v.findViewById(R.id.calendarView);
            widget.setOnDateChangedListener(this);
            elv = (ExpandableListView) v.findViewById(R.id.list);
            return v;
        }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        dateString = date.getYear() + "-"+ (date.getMonth() + 1) + "-" + date.getDay();
        elv.setAdapter(new SavedTabsListAdapter());
    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private ArrayList<String> groupList = db.getEvents(dateString);
        private ArrayList<ArrayList> childList = db.getChild(groupList);

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return childList.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            return groupList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return childList.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ScheduleFragment.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(72, 32, 0, 32);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ScheduleFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(16);
            textView.setPadding(96, 12, 0, 12);
            Linkify.addLinks(textView, Linkify.WEB_URLS);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

    }

        public void onViewCreated(View view, Bundle savedInstanceState) {


            ((NavDrawActivity) getActivity()).setActionBarTitle("Schedule");


            ArrayList<String> dayStrings = db.getAllEvents();

            ArrayList<CalendarDay> days = new ArrayList<>();

            for(int i = 0; i < dayStrings.size(); i++)
            {
                java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(dayStrings.get(i));
                    CalendarDay day = CalendarDay.from(date);
                    days.add(day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            widget.addDecorator(new EventDecorator(Color.rgb(255, 102, 89), days));


        }
}
