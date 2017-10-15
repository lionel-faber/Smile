package me.lionelfaber.jsontest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "smileDb";

    // Contacts table name
    private static final String TABLE_LINKS = "links";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_S_CODE = "scode";
    private static final String KEY_TYPE = "type";

    // Contacts table name
    private static final String TABLE_INFOS = "infos";

    // Contacts Table Columns names
    private static final String INFO_ID = "id";
    private static final String INFO_TITLE = "title";
    private static final String INFO_TYPE = "type";
    private static final String INFO_INFOS = "sub_infos";
    private static final String INFO_DATE = "date";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LINKS_TABLE = "CREATE TABLE " + TABLE_LINKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LINK + " TEXT," + KEY_S_CODE + " TEXT," + KEY_TYPE + " TEXT)";
        db.execSQL(CREATE_LINKS_TABLE);

        String CREATE_INFO_TABLE = "CREATE TABLE " + TABLE_INFOS + "(" + INFO_ID + " INTEGER PRIMARY KEY," + INFO_TITLE + " TEXT," + INFO_TYPE + " TEXT," + INFO_INFOS + " TEXT," + INFO_DATE + " TEXT)";
        db.execSQL(CREATE_INFO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFOS);

        // Create tables again
        onCreate(db);
    }

    public void addLink(Link link) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, link.getID());
        values.put(KEY_TITLE, link.getTitle());
        values.put(KEY_LINK, link.getLink());
        values.put(KEY_S_CODE, link.getScode());
        values.put(KEY_TYPE, link.getType());

        db.insert(TABLE_LINKS, null, values);
        db.close();
    }

    public void addInfo(Info info) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INFO_ID, info.get_id());
        values.put(INFO_TITLE, info.getTitle());
        values.put(INFO_TYPE, info.getInfoType());
        values.put(INFO_INFOS, info.getSubInfos());
        values.put(INFO_DATE, info.getDate());

        db.insert(TABLE_INFOS, null, values);
        db.close();
    }

    public boolean notExistsInfo(String s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String sql ="SELECT * FROM "+ TABLE_INFOS + " WHERE id="+s;
        cursor= db.rawQuery(sql,null);

        if(cursor.getCount()>0){
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<String> getAllInfo(String type) {
        ArrayList<String> infoList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INFOS + " WHERE type = \"" + type +"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                // Adding contact to list
                infoList.add(title);
            } while (cursor.moveToNext());
        }

        // return contact list
        return infoList;
    }

    public ArrayList<String> getEvents(String date) {
        ArrayList<String> eventList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INFOS + " WHERE type = \"2\" AND date = \"" + date +"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                // Adding contact to list
                eventList.add(title);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }

    public ArrayList<String> getAllEvents() {
        ArrayList<String> eventList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INFOS + " WHERE type = \"2\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(4);
                System.out.println(title);
                // Adding contact to list
                eventList.add(title);
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }

    public ArrayList<ArrayList> getChild(ArrayList<String> groupList)
    {
        ArrayList<ArrayList> children = new ArrayList<>();
        for(int i = 0; i < groupList.size(); i++)
        {
            ArrayList<String> childList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_INFOS + " WHERE title = \"" + groupList.get(i) +"\"";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                    String subinfo = cursor.getString(3);
                    childList.add(subinfo);
            }

            children.add(childList);

        }
        return children;
    }


    public boolean notExistsLink(String s)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String sql ="SELECT * FROM "+ TABLE_LINKS + " WHERE id="+s;
        cursor= db.rawQuery(sql,null);

        if(cursor.getCount()>0){
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<Link> getAllLinks(String sub) {
        ArrayList<Link> contactList = new ArrayList<Link>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LINKS + " WHERE scode = \"" + sub +"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Link contact = new Link();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setTitle(cursor.getString(1));
                contact.setLink(cursor.getString(2));
                contact.setScode(cursor.getString(3));
                contact.setType(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}