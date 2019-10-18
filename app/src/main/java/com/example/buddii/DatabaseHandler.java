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
public class DatabaseHandler extends SQLiteOpenHelper {
    //DATABASE SET UP
    // Version required
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Buddi_DATABASE";
    // User table name
    private static final String NAME_OF_TABLE = "MY_TABLE";
    // User Table Columns names
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_pass";
    private final Context context;

    //Creates  A table for every user. Passes TABLE NAME
    // Basically a concatination of SQL COMMANDS
    private String CREATE_TABLE = "CREATE TABLE " + NAME_OF_TABLE + "(" + USER_ID + " PlaceHolder," +
            USER_NAME + " PlaceHolder," + USER_EMAIL + " PlaceHolder," + USER_PASSWORD + " PlaceHolder " + ")";
    //Will Replace table if exist ( replace USER)
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_OF_TABLE;

    // Need a Databse HANDLER required
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase My_Database) {
        My_Database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase My_Database, int oldVersion, int newVersion) {
        My_Database.execSQL(DROP_TABLE);
        onCreate(My_Database);
    }
    // Values passed on from MainActivity,JAVA
    public void addemp(String userid, String name, String email, String password) {
        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.USER_ID, userid);
        values.put(DatabaseHandler.USER_NAME, name);
        values.put(DatabaseHandler.USER_EMAIL, email);
        values.put(DatabaseHandler.USER_PASSWORD, password);
        long status = My_Database.insert(NAME_OF_TABLE, null, values);

        if (status <= 0) {
            //TOAST ...ITS A CELEBRATION
            Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Insertion Successful", Toast.LENGTH_SHORT).show();
        }

        My_Database.close();

    }


    // DELETE A USER BY PASSING THE USERS ID
    public void deleteUser(String id) {
        int Tlength =Toast.LENGTH_SHORT;
        SQLiteDatabase My_Database = this.getWritableDatabase();
    // delete user record by id
        long TempLong = My_Database.delete(NAME_OF_TABLE, USER_ID + " = ?",
                new String[]{String.valueOf(id)});
        if (TempLong <= 0) {
            Toast.makeText(context, "Deletion Failed ",
            Tlength).show();
        } else {
            Toast.makeText(context, "USER DELETED!",
            Tlength).show();
        }
        My_Database.close();
    }


    //fetches all records of the Table stored in the Database.
    //Uses a cursor (learned in CINS 570)
    public String load() {

        String result = "";
        String query = "Select*FROM " + NAME_OF_TABLE;

        SQLiteDatabase My_Database = this.getWritableDatabase();
        Cursor cursor = My_Database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String results1 = cursor.getString(1);
            String results2 = cursor.getString(2);
            String results3 = cursor.getString(3);
            String r0 =String.valueOf(result_0);
            // concat results
            result += r0 + " " + results1 + " " + results2 + " " + results3 + "\n";
            System.getProperty("line.separator");
        }
        cursor.close();
        My_Database.close();
        // if nothing was appended then TOAST this
        if (result == "" ) {
            Toast.makeText(context, "USER NOT CREATED YET", Toast.LENGTH_SHORT).show();
        }
        return result;

    }


}
