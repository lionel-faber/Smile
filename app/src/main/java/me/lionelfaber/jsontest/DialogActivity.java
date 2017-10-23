package me.lionelfaber.jsontest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by lionel on 23/10/17.
 */

public class DialogActivity extends android.app.DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Enter the Semester");
            String[] semesters = { "4", "6" };
            final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, semesters);
            final Spinner  spinner= new Spinner(getActivity());
            spinner.setAdapter(adp);
            builder.setView(spinner);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), "Selected semester " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor =  pref.edit();
                    editor.putString("semester", spinner.getSelectedItem().toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), NavDrawActivity.class));
                }
            });
            setCancelable(false);
            return builder.create();
        }
    }