package com.example.buddii;


/*
this JAVA file is used to manage database creation
including and version management and is able to use many SQL methods
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


// SQLITE set up to handle DATABASE
public class MyDBHandler extends SQLiteOpenHelper {
    //DATABASE SET UP
    // Version required
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Buddi_DATABASE";
    // User table name
    private static final String TABLE_NAME = "MY_TABLE";
    // User Table Columns names
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_pass";
    private final Context context;

    //Creates  A table for every user. Passes TABLE NAME
    // Basically a concatination of SQL COMMANDS
    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + USER_ID + " PlaceHolder," +
            USER_NAME + " PlaceHolder," + USER_EMAIL + " PlaceHolder," + USER_PASSWORD + " PlaceHolder " + ")";
    //Will Replace table if exist ( replace USER)
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Need a DBHANDLER required
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    // Values passed on from MainActivity,JAVA
    public void addemp(String userid, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDBHandler.USER_ID, userid);
        values.put(MyDBHandler.USER_NAME, name);
        values.put(MyDBHandler.USER_EMAIL, email);
        values.put(MyDBHandler.USER_PASSWORD, password);
        long status = db.insert(TABLE_NAME, null, values);

        if (status <= 0) {
            //TOAST ...ITS A CELEBRATION
            Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Insertion Successful", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }


    // DELETE A USER BY PASSING THE USERS ID
    public void deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
// delete user record by id
        long s = db.delete(TABLE_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        if (s <= 0) {
            Toast.makeText(context, "Deletion NOT successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "USER DELETED!", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //fetches all records of the Table stored in the Database.
    //Uses a cursor (learned in CINS 570)
    public String load() {

        String result = "";
        String query = "Select*FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String r1 = cursor.getString(1);
            String r2 = cursor.getString(2);
            String r3 = cursor.getString(3);
            // concat results
            result += String.valueOf(result_0) + " " + r1 + " " + r2 + " " + r3 + "\n";
            System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        // if nothing was appended then TOAST this
        if (result == "" ) {
            Toast.makeText(context, "USER NOT CREATED YET", Toast.LENGTH_SHORT).show();
        }
        return result;

    }


}