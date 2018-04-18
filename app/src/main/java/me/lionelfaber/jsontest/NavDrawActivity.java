package me.lionelfaber.jsontest;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class NavDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String url1, url2, semester;
    DatabaseHandler db;
    private static final int RC_SIGN_IN = 1704;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);
        semester = sharedPreferences.getString("semester",null);

        if (auth.getCurrentUser() != null && semester != null) {
            // already signed in

            db  = new DatabaseHandler(this);
            url1 = "http://smileit.pythonanywhere.com/api/get/subs/" + semester;
            url2 = "http://smileit.pythonanywhere.com/api/get/infos/" + semester;


            makeJsonArrayRequest1();
            makeJsonArrayRequest2();


            String b = getIntent().getStringExtra("fragment");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(b != null) {
                int target = Integer.parseInt(b);
                switch (target) {
                    case 1:
                        ft.replace(R.id.your_placeholder, new InformationFragment());
                        break;
                    case 2:
                        ft.replace(R.id.your_placeholder, new ScheduleFragment());
                        break;
                    case 3:
                        ft.replace(R.id.your_placeholder, new PlacementFragment());
                        break;
                    default:
                        ft.replace(R.id.your_placeholder, new SubjectFragment());
                        this.setActionBarTitle("Subjects");
                        break;
                }
            }
            else
            {
                ft.replace(R.id.your_placeholder, new SubjectFragment());
                this.setActionBarTitle("Subjects");
            }
            ft.commit();
        } else {
            // not signed in
            startLogin();


        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView smile = (ImageView)header.findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.smile).into(smile);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                showDialog();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Action Cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No Internet Connection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar("Error! Try Again");
                    return;
                }
            }

            showSnackbar("Unknown Response");
        }
    }

    private boolean showDialog() {
        FragmentManager manager = getFragmentManager();
        DialogActivity dialogActivity;
        dialogActivity = new DialogActivity();
        dialogActivity.show(manager, "DialogActivity");
        return true;
    }

    public void startLogin()
    {
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.smile)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    public void showSnackbar(String message)
    {
       CoordinatorLayout rootlayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(rootlayout , message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void makeJsonArrayRequest1(){

        JsonArrayRequest req = new JsonArrayRequest(url1,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject l = (JSONObject) response.get(i);

                                String title = l.getString("title");
                                String link = l.getString("link");
                                String code = l.getString("subject_id");
                                String type = l.getString("notesType");
                                String idd = l.getString("id");

                                if(db.notExistsLink(idd))
                                {
                                    db.addLink(new Link(Integer.parseInt(idd), title, link, code, type));
                                    Log.d("Adding Link to DB", "ID: " + idd + " Title: " + title + " Link: " + link + " Type: " + type);
                                }



                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

//                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void makeJsonArrayRequest2(){

        JsonArrayRequest req = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject l = (JSONObject) response.get(i);

                                String idd = l.getString("id");
                                String title = l.getString("title");
                                String type = l.getString("infoType");
                                String infos = l.getString("subInfos");
                                String date = l.getString("date");

                                if(db.notExistsInfo(idd))
                                {
                                    db.addInfo(new Info(Integer.parseInt(idd), title, type, infos, date));
                                    Log.d("Adding info to DB", "ID: " + idd + " Title: " + title + " Type: " + type + " SubInfos" + infos + " Date " + date);

                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

//                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

//    private void showpDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hidepDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int c = getSupportFragmentManager().getBackStackEntryCount();
//            Log.d("count", "" + c);

            if (c == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Close Application")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finishAffinity();
                            }
                        }).create().show();
            } else {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }


    public void setActionBarTitle(String title){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if(semester == null)
        {
            Toast.makeText(getApplicationContext(), "You must login first!", Toast.LENGTH_LONG).show();
            FragmentManager manager = getFragmentManager();
            DialogActivity dialogActivity;
            dialogActivity = new DialogActivity();
            dialogActivity.show(manager, "DialogActivity");
        }
        else {

            int id = item.getItemId();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            if (id == R.id.nav_subjects) {
                // Handle the camera action
                ft.replace(R.id.your_placeholder, new SubjectFragment());
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_info) {
                ft.replace(R.id.your_placeholder, new InformationFragment());
                ft.addToBackStack(null);
                ft.commit();

            } else if (id == R.id.schedule) {

                ft.replace(R.id.your_placeholder, new ScheduleFragment());
                ft.addToBackStack(null);
                ft.commit();


            } else if (id == R.id.placement) {

                ft.replace(R.id.your_placeholder, new PlacementFragment());
                ft.addToBackStack(null);
                ft.commit();

            } else if (id == R.id.contact) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "14itsjit@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report from SMILE App");
                startActivity(intent);

            } else if (id == R.id.signout) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                String s = pref.getString("semester",null);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("semester", null);
                editor.apply();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("semester" + s);

                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(NavDrawActivity.this, NavDrawActivity.class));
                                finish();
                            }
                        });
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


