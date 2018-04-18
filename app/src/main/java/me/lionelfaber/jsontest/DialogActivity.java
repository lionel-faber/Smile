package me.lionelfaber.jsontest;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

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
            final Spinner spinner= new Spinner(getActivity());
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10, 0, 10, 0);
            spinner.setAdapter(adp);
            linearLayout.addView(spinner);
            final TextInputEditText passwordField = new TextInputEditText(getActivity());
            passwordField.setHint("Password");
            linearLayout.addView(passwordField);
            builder.setView(linearLayout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String password = passwordField.getText().toString();
                    if(password.equals("keepsmiling")) {
                        Toast.makeText(getActivity(), "Selected semester " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("semester", spinner.getSelectedItem().toString());
                        editor.apply();
                        FirebaseMessaging.getInstance().subscribeToTopic("semester"+spinner.getSelectedItem().toString());
                        startActivity(new Intent(getActivity(), NavDrawActivity.class));
                    }
                    else {
                        Toast.makeText(getActivity(), "Wrong password", Toast.LENGTH_LONG).show();
                        FragmentManager manager = getFragmentManager();
                        DialogActivity dialogActivity;
                        dialogActivity = new DialogActivity();
                        dialogActivity.show(manager, "DialogActivity");
                    }
                }
            });
            setCancelable(false);
            return builder.create();
        }
    }