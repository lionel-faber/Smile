package me.lionelfaber.jsontest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NavDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog pDialog;
    String url1, url2;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);


//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Syncing database");
//        pDialog.setCancelable(false);
//
//        showpDialog();

        db  = new DatabaseHandler(this);
        url1 = "http://192.168.1.6:8000/get/subs/6";
        url2 = "http://192.168.1.6:8000/get/infos/6";


        makeJsonArrayRequest1();
        makeJsonArrayRequest2();


//        hidepDialog();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, new SubjectFragment());
        ft.commit();
        this.setActionBarTitle("Subjects");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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

        } else if (id == R.id.contact) {

            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "14itsjit@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Report from SMILE App");
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
