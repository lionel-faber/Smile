package me.lionelfaber.jsontest;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 30/5/17.
 */

public class SubjectFragment extends Fragment {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;
    String semester;


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.subject_fragment, parent, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((NavDrawActivity) getActivity()).setActionBarTitle("Subjects");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(getActivity(), subjectList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref",0);
        semester = sharedPreferences.getString("semester",null);
        if(semester.equals("6"))
            prepareSubjects6();
        else
            prepareSubjects4();

    }

    private void prepareSubjects4() {
        int[] covers = new int[]{
                R.drawable.os,
                R.drawable.daa,
                R.drawable.se,
                R.drawable.micro,
                R.drawable.pqt,
                R.drawable.micro,
                R.drawable.os,
                R.drawable.se};

        Subject a = new Subject("Operating Systems", "CS6401", covers[0]);
        subjectList.add(a);

        a = new Subject("Design and Algo Analysis", "CS6402", covers[1]);
        subjectList.add(a);

        a = new Subject("Software Engineering", "CS6403", covers[2]);
        subjectList.add(a);

        a = new Subject("Microprocessor & Microcontrollers", "EC6504", covers[3]);
        subjectList.add(a);

        a = new Subject("Probability and Queuing theory ", "MA6453", covers[4]);
        subjectList.add(a);

        a = new Subject("MPMC Laboratory", "IT6411", covers[5]);
        subjectList.add(a);

        a = new Subject("Operating Systems Lab", "IT6412", covers[6]);
        subjectList.add(a);

        a = new Subject("Software Engineering Lab", "IT6413", covers[7]);
        subjectList.add(a);

        adapter.notifyDataSetChanged();
    }

    private void prepareSubjects6() {
        int[] covers = new int[]{
                R.drawable.art,
                R.drawable.compiler,
                R.drawable.soft,
                R.drawable.ds,
                R.drawable.mc,
                R.drawable.total,
                R.drawable.cdl,
                R.drawable.madlabb};

        Subject a = new Subject("Artificial Intelligence", "CS6659", covers[0]);
        subjectList.add(a);

        a = new Subject("Compiler Design", "CS6660", covers[1]);
        subjectList.add(a);

        a = new Subject("Software Architecture", "IT6602", covers[2]);
        subjectList.add(a);

        a = new Subject("Distributed Systems", "CS6601", covers[3]);
        subjectList.add(a);

        a = new Subject("Mobile Computing", "IT6601", covers[4]);
        subjectList.add(a);

        a = new Subject("Total Quality Management", "GE6757", covers[5]);
        subjectList.add(a);

        a = new Subject("Compiler Design Lab", "CS6612", covers[6]);
        subjectList.add(a);

        a = new Subject("Mobile Application Development Lab", "CS6611", covers[7]);
        subjectList.add(a);

        adapter.notifyDataSetChanged();
    }



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
