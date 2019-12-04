package com.example.buddii;


/*
this JAVA file is used to manage database creation
including and version management and is able to use many SQL methods
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;


// SQLITE set up to handle DATABASE
public class DatabaseHandler extends SQLiteOpenHelper {
    //DATABASE SET UP
    // Version required
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Buddi_DATABASE";
    // User table name
    private static final String NAME_OF_TABLE = "USERS_TABLE";
    private static final  String NAME_OF_FRIENDS_TABLE= "FRIENDS_TABLE";

    // Preping Users Tables Column names
    private static final String USER_PHONE = "user_phone";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_pass";



    /* Preping for GPS DB */
     private static final String  LATITUDE = "latitude";
     private static final String LONGITUTDE = "longitude";
     private static final String GPS_TABLE = "GPS_TABLE";
    /* GPS DB */

    // temp plz delete
    String jsonString2 ="";
    private final Context context;



    //Preping Strings for Creation/deletion/altering of Databases
    // Basically a concatination of SQL COMMANDS

    private String CREATE_TABLE = "CREATE TABLE " + NAME_OF_TABLE + "( Uid INTEGER primary key autoincrement," + USER_PHONE + " PlaceHolder," +
            USER_NAME + " PlaceHolder," + USER_EMAIL + " PlaceHolder," + USER_PASSWORD + " PlaceHolder " + ")";

    private String CREATE_FRIENDS_TABLE = "CREATE TABLE " + NAME_OF_FRIENDS_TABLE + "( Uid INTEGER)";


    private String CREATE_GPS_TABLE = "CREATE TABLE " + GPS_TABLE + "(" + LATITUDE + " PlaceHolder," +
            LONGITUTDE + " PlaceHolder"  + ")";

    //Will Replace table if exist ( replace USER)
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_OF_TABLE;

    //testing of insert to dynamic colum
    String friend_col = "friend1";
    String friend_col2 = "friend2";
    private String INSERT_DYNAMIC_TABLE = "ALTER TABLE " + NAME_OF_FRIENDS_TABLE + " ADD COLUMN "+ friend_col +" INT";
    private String INSERT_DYNAMIC_TABLE2 = "ALTER TABLE " + NAME_OF_FRIENDS_TABLE + " ADD COLUMN "+ friend_col2 +" INT";


    // Need a Databse HANDLER required
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase My_Database) {
        //execute prepared commands
        My_Database.execSQL(CREATE_TABLE);
        My_Database.execSQL(CREATE_GPS_TABLE);
        My_Database.execSQL(CREATE_FRIENDS_TABLE);

        // testing of dynamic colums
        My_Database.execSQL(INSERT_DYNAMIC_TABLE);
        My_Database.execSQL(INSERT_DYNAMIC_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase My_Database, int oldVersion, int newVersion) {
        My_Database.execSQL(DROP_TABLE);
        onCreate(My_Database);
    }



    // Values passed on from MainActivity,JAVA
    //values here will be placed into the USERS TABLE
    public void addToDb(String userphone, String name, String email, String password) throws NoSuchAlgorithmException {

        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.USER_PHONE, userphone);
        values.put(DatabaseHandler.USER_NAME, name);
        values.put(DatabaseHandler.USER_EMAIL, email);

        //password will be sent to function and hashed
        String shaPword = hashSha512.hashPaswordSHA512(password);
        values.put(DatabaseHandler.USER_PASSWORD, shaPword);

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
    // delete user record by phone
        long TempLong = My_Database.delete(NAME_OF_TABLE, USER_PHONE + " = ?",
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
    // values will be returned in the format/order they are requested via array
    //can be One or Many attributes requested
    public String[] loadUsers(String requestCall) {
        SQLiteDatabase My_Database = this.getWritableDatabase();
        /* get number of rows */

       String[] requestHolderArray = requestCall.split(",");
       int numOfRequest = requestHolderArray.length;
        int count =0;
       // array of Indeces to hold format of strings
        String[] arrayOfIndecesHolder =new String[5];

        int numOfUsers = getNumOfUsers();
        String ArrayOfresult[] = new String[numOfUsers];


       String result = "";
       String SelectAll = "SELECT*FROM ";
       String command = SelectAll + NAME_OF_TABLE;

        Cursor cursor = My_Database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            String Uid = cursor.getString(0);
            String phoneNumber = cursor.getString(1);
            String name = cursor.getString(2);
            String email = cursor.getString(3);
            //String password = cursor.getString(3);

            // for loop will place the order of string according to the order
            // the user request

            for (int i = 0 ; i < numOfRequest; i++) {
                if (requestHolderArray[i].equals("name")) {
                    arrayOfIndecesHolder[i] = name;
                }
                if (requestHolderArray[i].equals("phoneNumber")) {
                    arrayOfIndecesHolder[i] = phoneNumber;
                }
                if (requestHolderArray[i].equals("email")) {
                    arrayOfIndecesHolder[i] = email;
                }
                if (requestHolderArray[i].equals("Uid")) {
                    arrayOfIndecesHolder[i] = Uid;
                }
            }
            for (int i = 0 ; i < numOfRequest; i++) {

                result += arrayOfIndecesHolder[i] + " ";
                if (i == (numOfRequest- 1))
                {
                    ArrayOfresult[count] = result;

                }
            }

            result="";
            count++;

           }
            System.getProperty("line.separator");

        cursor.close();
        My_Database.close();
        // if nothing was appended then TOAST this
        if (ArrayOfresult[0] == null ) {
            Toast.makeText(context, "USER NOT CREATED YET", Toast.LENGTH_SHORT).show();
        }

        return ArrayOfresult;

    }



    // this function returns the number of users in the database
    public int getNumOfUsers(){
        SQLiteDatabase My_Database = this.getWritableDatabase();
        //awsome Static utility methods for dealing with databases, only returns a double
        long tempTotalrows = DatabaseUtils.queryNumEntries(My_Database,NAME_OF_TABLE,null);
        int totalrows= (int) tempTotalrows;
        return totalrows;
    }
    /* --------------- FOR GPS DATABASE -----------   */
    /*  EXAMPLE OF FUNCTION CALL TO ADD TO GPS DATABASE FROM ANOTHER JAVA FILE (MapsActivity in this case) :

        double latitude = 11.12345;
        double longitude = 999.7900000;

        DatabaseHandler handler=new DatabaseHandler(MapsActivity.this);
        handler.addGPS(latitude,longitude);

     */

    public void addGPS (Double Alatitude ,Double Alongitude)
    {
        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.LONGITUTDE,Alongitude );
        values.put(DatabaseHandler.LATITUDE, Alatitude);

        long status = My_Database.insert(GPS_TABLE, null, values);

        if (status <= 0) {
            //TOAST ...ITS A CELEBRATION
            Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Insertion Successful", Toast.LENGTH_SHORT).show();
        }

        My_Database.close();


    }

    public String loadGPS() {

        String result = "";
        String SelectAll = "SELECT*FROM ";
        String command = SelectAll + GPS_TABLE;

        SQLiteDatabase My_Database = this.getWritableDatabase();
        Cursor cursor = My_Database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            String result0 = cursor.getString(0);
            String results1 = cursor.getString(1);
            // concat results
             result += "Latitude: " + result0 + " Longitude: " + results1 + " " + "\n";

        }
        System.getProperty("line.separator");

        cursor.close();
        My_Database.close();
        // if nothing was appended then TOAST this
        if (result == "" ) {
            Toast.makeText(context, "NO LONG OR LAT IN DB", Toast.LENGTH_SHORT).show();
        }
        return result;

    }

    //
    public Cursor getAllData() {
        String selectQuery = "Select * from "+NAME_OF_TABLE;
        SQLiteDatabase My_Database = this.getReadableDatabase();
        Cursor cursor = My_Database.rawQuery(selectQuery, null);
        return cursor;
    }

    // this function will create a JSON file and prepare it to send to an online DB
  public void sendtoOnlineDB()  {


      Cursor cursor = getAllData();  //cursor hold all your data
      JSONObject jobj ;
      JSONArray arr = new JSONArray();
         while(cursor.moveToNext()) {

            jobj  = new JSONObject();
          try {
              jobj.put("uID",cursor.getString(0));
          } catch (JSONException e) {
              e.printStackTrace();
          }
          try {
              jobj.put("user_phone",cursor.getString(1));
          } catch (JSONException e) {
              e.printStackTrace();
          }
          try {
              jobj.put("user_name",cursor.getString(2));
          } catch (JSONException e) {
              e.printStackTrace();
          }
          try {
              jobj.put("user_email",cursor.getString(3));
          } catch (JSONException e) {
              e.printStackTrace();
          }
          try {
              jobj.put("user_pass",cursor.getString(4));
          } catch (JSONException e) {
              e.printStackTrace();
          }
          arr.put(jobj);
      }

      jobj = new JSONObject();
      try {
          jobj.put("data", arr);
      } catch (JSONException e) {
          e.printStackTrace();
      }

       jsonString2 = jobj.toString();

  };
    // for JSON testing , this will get called from DBActivity and display on app
    public String mytempJSONreturnFunc(){
      String newst = jsonString2 ;
        return newst;
    };




}
