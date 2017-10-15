package me.lionelfaber.jsontest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by lionel on 14/10/17.
 */

public class InformationFragment extends Fragment {

    DatabaseHandler db;



    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.info_layout, null);
        db  = new DatabaseHandler(getContext());
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.list);
        elv.setAdapter(new SavedTabsListAdapter());
        return v;
    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private ArrayList<String> groupList = db.getAllInfo("1");
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
            TextView textView = new TextView(InformationFragment.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(72, 32, 0, 32);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(InformationFragment.this.getActivity());
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

        ((NavDrawActivity) getActivity()).setActionBarTitle("Information");


    }
}
