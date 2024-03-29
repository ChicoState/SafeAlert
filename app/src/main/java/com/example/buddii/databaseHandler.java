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
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// SQLITE set up to handle DATABASE
public class databaseHandler extends SQLiteOpenHelper {
    //DATABASE SET UP
    // Version required
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Buddi_DATABASE";
    // User table name
    private static final String NAME_OF_USERS_TABLE = "USERS_TABLE";
    private static final  String NAME_OF_FRIENDS_TABLE= "FRIENDS_TABLE";
    private static final String NAME_OF_ACTIVE_BUDDI_TABLE = "ACTIVE_BUDDI_TABLE";
    private static final String NAME_OF_FLAG_TABLE = "FLAG_TABLE";

    // Prepping Users Tables Column names
    private static final String USER_PHONE = "user_phone";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD = "user_pass";
    private static final String USER_SALT = "user_salt";
    private static final String USER_RATINGS = "user_ratings";
    private static final String USER_FLAG = "user_flag";

    /* Prepping for GPS DB */
    private static final String  LATITUDE = "latitude";
    private static final String LONGITUTDE = "longitude";
    private static final String GPS_TABLE = "GPS_TABLE";
    /* GPS DB */


    String jsonString2 ="";
    private final Context context;
    // for hashing
    String shaPword = "";

    // Gobal variable
    public static String shaPwordToCompare = "";
    public static byte[] salt = null;

    public static JSONObject usersObj ;
    public static JSONArray arr1 = new JSONArray();
    public static String passwordStoredToCheck = "";
    public static byte[] salt5 = null;
    public static JSONObject jobjForGPS ;
    public static JSONArray arrForGPS = new JSONArray();
    public static JSONObject jobjForFlag ;
    public static JSONArray arrForFlag = new JSONArray();




    //Preping Strings for Creation/deletion/altering of Databases
    // Basically a concatination of SQL COMMANDS

    private String CREATE_TABLE = "CREATE TABLE " + NAME_OF_USERS_TABLE + "( Uid INTEGER primary key autoincrement," + USER_PHONE + " PlaceHolder," +
            USER_NAME + " PlaceHolder," + USER_EMAIL + " PlaceHolder," + USER_PASSWORD + " PlaceHolder ," + USER_RATINGS+ "," + USER_SALT + ","+ USER_FLAG +  ")";

    private String CREATE_FRIENDS_TABLE = "CREATE TABLE " + NAME_OF_FRIENDS_TABLE + "( Uid INTEGER)";
    private String CREATE_ACTIVE_BUDDII_TABLE = "CREATE TABLE " + NAME_OF_ACTIVE_BUDDI_TABLE + "( Uid INTEGER)";
    private String CREATE_FLAG_TABLE = "CREATE TABLE " + NAME_OF_FLAG_TABLE + "( Uid INTEGER)";


    private String CREATE_GPS_TABLE = "CREATE TABLE " + GPS_TABLE + "( Uid INTEGER primary key autoincrement," + LATITUDE + " PlaceHolder," +
            LONGITUTDE + " PlaceHolder"  + ")";

    //Will Replace table if exist ( replace USER)
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_OF_USERS_TABLE;

    //testing of insert to dynamic colum
    String friend_col = "friend1";
    String friend_col2 = "friend2";
    private String INSERT_DYNAMIC_TABLE = "ALTER TABLE " + NAME_OF_FRIENDS_TABLE + " ADD COLUMN "+ friend_col +" INT";
    private String INSERT_DYNAMIC_TABLE2 = "ALTER TABLE " + NAME_OF_FRIENDS_TABLE + " ADD COLUMN "+ friend_col2 +" INT";


    // Need a Databse HANDLER required
    public databaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase My_Database) {

        //execute prepared commands
        My_Database.execSQL(CREATE_TABLE);
        My_Database.execSQL(CREATE_GPS_TABLE);
        My_Database.execSQL(CREATE_FRIENDS_TABLE);
        My_Database.execSQL(CREATE_ACTIVE_BUDDII_TABLE);


        // testing of dynamic colums
        My_Database.execSQL(INSERT_DYNAMIC_TABLE);
        My_Database.execSQL(INSERT_DYNAMIC_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase My_Database, int oldVersion, int newVersion) {
        My_Database.execSQL(DROP_TABLE);
        onCreate(My_Database);
    }

    // Values passed on from mainActivity,JAVA
    //values here will be placed into the USERS TABLE

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addToDb(String userphone, String name, String email, String password, String salt0 ,Boolean flag)  {
         salt = hashSha512.getSalt();

         // temp flag
        String flagForUser = "none";

        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor c = My_Database.rawQuery("SELECT * FROM USERS_TABLE where user_name = " + " '" + name + "'", null);
    // set flag condition here so wont taoast so much
        if(c.getCount()>0 )
        {
            return;
        }

        values.put(databaseHandler.USER_PHONE, userphone);
        values.put(databaseHandler.USER_NAME, name);
        values.put(databaseHandler.USER_EMAIL, email);
        //password will be sent to function and hashed


         shaPword = hashSha512.hashPaswordSHA512(password,salt);
         //prep for storing
         shaPwordToCompare = shaPword.substring(0,128);


        values.put(databaseHandler.USER_PASSWORD, shaPword);

        // in order to compare users password, we need to store the original SALT which was used to
        //hash the password. Otherwise a new SALT would be generated and would result in a differnt hash.

        String byteSaltToString = Base64.getEncoder().encodeToString(salt);
        values.put(databaseHandler.USER_SALT, byteSaltToString);
        values.put(databaseHandler.USER_FLAG, flagForUser);


        long status = My_Database.insert(NAME_OF_USERS_TABLE, null, values);



        if (status <= 0) {
           Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
        }

        // this flag will indicated that the function is being called from the register page
        // and not from the initial sync from log in ( removes repetative inserts)
        if (flag == false) {
            setLocalDBToJSON();
            String Jsonxx = mytempJSONreturnFunc();
            // sending to ONLINE firebase DB
            FirebaseDatabase database = FirebaseDatabase.getInstance();


            Map<String, Object> jsonMap = new Gson().fromJson(Jsonxx, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public String checkFireBaseDBForUsers(){
           String jsonToSend = "nothing";

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("usersdb").child("users").child("data");

            myRef.addChildEventListener(new ChildEventListener() {


                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String uID = dataSnapshot.child("uID").getValue(String.class);


                    String email = dataSnapshot.child("user_email").getValue(String.class);
                    if (email == null) {

                        return;
                    }
                    ;
                    String namex = dataSnapshot.child("user_name").getValue(String.class);
                    String pass = dataSnapshot.child("user_pass").getValue(String.class);
                    String phone = dataSnapshot.child("user_phone").getValue(String.class);
                    String salt1 = dataSnapshot.child("user_salt").getValue(String.class);


                    usersObj = new JSONObject();
                    try {

                        usersObj.put("uID", uID);

                        usersObj.put("user_phone", phone);

                        usersObj.put("user_name", namex);

                        usersObj.put("user_email", email);

                        usersObj.put("user_pass", pass);

                        usersObj.put("user_salt", salt1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    arr1.put(usersObj);

                } //end of loop

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            usersObj = new JSONObject();
            try {
                usersObj.put("data", arr1);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            jsonToSend = usersObj.toString();
            usersObj=null;


            dbSYNC(jsonToSend);

            return jsonToSend;


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dbSYNC(String jsonString)  {

        try {
            JSONObject jsnobject = new JSONObject(jsonString);
            JSONArray jsonArray = jsnobject.getJSONArray("data");
            String phone0 ="";
            String name0 ="";
            String email0 ="";
            String pass0 ="";
            String salt0 ="";
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject rec = jsonArray.getJSONObject(i);
                 phone0 = rec.getString("user_phone");
                 name0 = rec.getString("user_name");
                 email0 = rec.getString("user_email");
                 pass0 = rec.getString("user_pass");
                 salt0 = rec.getString("user_salt");

                // need to set UID correct and check to see if UID is already In DB, no DUplicates
                addToDb(phone0,name0,email0,pass0,salt0,true);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // DELETE A USER BY PASSING THE USERS ID
    public void deleteUser(String id) {
        int Tlength =Toast.LENGTH_SHORT;
        SQLiteDatabase My_Database = this.getWritableDatabase();
        // delete user record by phone
        long TempLong = My_Database.delete(NAME_OF_USERS_TABLE, USER_PHONE + " = ?",
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
        String command = SelectAll + NAME_OF_USERS_TABLE;

        Cursor cursor = My_Database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            String Uid = cursor.getString(0);
            String phoneNumber = cursor.getString(1);
            String name = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);
            String salt3 = cursor.getString(6);

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
                if (requestHolderArray[i].equals("user_pass")) {
                    arrayOfIndecesHolder[i] = password;
                }
                if (requestHolderArray[i].equals("salt")) {
                    arrayOfIndecesHolder[i] = salt3;
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

    //get friends from specific user

    public String[] loadFriends(String requestCall) {
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
        String command = SelectAll + NAME_OF_FRIENDS_TABLE;

        Cursor cursor = My_Database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            String Uid = cursor.getString(0);
            String phoneNumber = cursor.getString(1);
            String name = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);
            String salt3 = cursor.getString(6);

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
                if (requestHolderArray[i].equals("user_pass")) {
                    arrayOfIndecesHolder[i] = password;
                }
                if (requestHolderArray[i].equals("salt")) {
                    arrayOfIndecesHolder[i] = salt3;
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
            Toast.makeText(context, "YOU HAVE NO FRIENDS", Toast.LENGTH_SHORT).show();
        }

        return ArrayOfresult;
    }

    // this function returns the number of users in the database
    public int getNumOfUsers(){
        SQLiteDatabase My_Database = this.getWritableDatabase();
        //awsome Static utility methods for dealing with databases, only returns a double
        long tempTotalrows = DatabaseUtils.queryNumEntries(My_Database,NAME_OF_USERS_TABLE,null);
        int totalrows= (int) tempTotalrows;
        return totalrows;
    }
    /* --------------- FOR ` DATABASE -----------   */
    /*  EXAMPLE OF FUNCTION CALL TO ADD TO GPS DATABASE FROM ANOTHER JAVA FILE (MapsActivity in this case) :

        double latitude = 11.12345;
        double longitude = 999.7900000;

        databaseHandler handler=new databaseHandler(MapsActivity.this);
        handler.addGPS(latitude,longitude);

     */


    //
    public Cursor getAllData(String fromThisDB) {

        if (fromThisDB == "userTable")
        {

            SQLiteDatabase My_Database = this.getReadableDatabase();
            String selectQuery = "Select * from "+NAME_OF_USERS_TABLE;
            Cursor cursor = My_Database.rawQuery(selectQuery, null);
            return cursor;
        }
        if (fromThisDB == "friendsTable")
        {
            String selectQuery = "Select * from "+NAME_OF_FRIENDS_TABLE;
            SQLiteDatabase My_Database = this.getReadableDatabase();
            Cursor cursor = My_Database.rawQuery(selectQuery, null);
            cursor.close();
            return cursor;
        }
        else {

            Cursor o = null;
            return o;
        }


    }

    // this function will create a JSON file and prepare it to send to an online DB
    public void setLocalDBToJSON()  {


        Cursor cursor = getAllData("userTable");  //cursor hold all your data
        JSONObject jobj ;
        JSONArray arr = new JSONArray();
        while(cursor.moveToNext()) {

            jobj  = new JSONObject();
            try {
                jobj.put("uID",cursor.getString(0));

                jobj.put("user_phone",cursor.getString(1));

                jobj.put("user_name",cursor.getString(2));

                jobj.put("user_email",cursor.getString(3));

                jobj.put("user_pass",cursor.getString(4));

                jobj.put("user_ratings",cursor.getString(5));

                jobj.put("user_salt",cursor.getString(6));

                jobj.put("user_flag",cursor.getString(7));
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

    }
    // for JSON testing , this will get called from dbActivity and display on app
    public String mytempJSONreturnFunc(){
        String newst = jsonString2 ;
        return newst;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPword(String pwordFromLogIn, final String userNameFromLogIn) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("usersdb").child("users").child("data");

        myRef.addChildEventListener(new ChildEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String uID = dataSnapshot.child("uID").getValue(String.class);
                String namex = dataSnapshot.child("user_name").getValue(String.class);
                String pass = dataSnapshot.child("user_pass").getValue(String.class);
                String salt0 = dataSnapshot.child("user_salt").getValue(String.class);


                String t_nameFromDB = namex.substring(0);


                if (t_nameFromDB.equals(userNameFromLogIn)){

                    passwordStoredToCheck = pass;
                    salt5 = Base64.getDecoder().decode(salt0);


              }


            } //end of loop

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //                                 INPUT PWORD        PWORDFROMFIREBASE  SLATFROMFIREBASE
        String checkCred = checkCredentials(pwordFromLogIn, passwordStoredToCheck,  salt5);

        return checkCred;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String checkCredentials(String checkThisPassword, String pwordInDB, byte[] salt6){
        if(salt6 == null){
                return "false";
        }

        String pwordToHash = checkThisPassword;

        String shaPwordToCheck = "";
        shaPwordToCheck = hashSha512.hashPaswordSHA512(pwordToHash, salt6);

        // for an UNKNOWN reason the strings have to be substrings in order for the
        // comparisons to work
        String t_pwordInDB =pwordInDB.substring(0,128);
        String t_shaPwordToCheck = shaPwordToCheck.substring(0,128);



        if (t_pwordInDB.equals(t_shaPwordToCheck))
        {

            String good = "true";
            return good;
        }
        else {
            String noGood = "false";
            return noGood;
        }
    }
    public void addGPS (Double t_latitude ,Double t_longitude) throws JSONException {
        Integer userID = 0;

        jobjForGPS  = new JSONObject();;
        jobjForGPS.put("uID",userID);
        jobjForGPS.put("latitude",t_latitude );
        jobjForGPS.put("longitude",t_longitude );
        arrForGPS.put(jobjForGPS);

        jobjForGPS = new JSONObject();
        jobjForGPS.put("gpsdata", arrForGPS);

        String stringGPSToSend = jobjForGPS.toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Map<String, Object> jsonMap = new Gson().fromJson(stringGPSToSend, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public static  String[] posZeroLatPosOneLong = new String[3];
    public String[] loadGPS(final String passedUID) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("dbgps").child("gpsdata");

        myRef.addChildEventListener(new ChildEventListener() {



            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Integer uID = dataSnapshot.child("uID").getValue(Integer.class);
                Double d1 = (Double) dataSnapshot.child("latitude").getValue();
                Double d2 = (Double) dataSnapshot.child("longitude").getValue();

                String converted_Lat =String.valueOf(d1);
                String converted_Long = String.valueOf(d2);

                String converted_UID = String.valueOf(uID);

                if(passedUID.equals(converted_UID)){

                    posZeroLatPosOneLong[0]= converted_Lat;
                    posZeroLatPosOneLong[1]= converted_Long;

                }
            } //end of loop

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return posZeroLatPosOneLong;
    }


    public void sendFlag(String userID,String thisFlag) throws JSONException {

        jobjForFlag  = new JSONObject();;
        jobjForFlag.put("UID",userID);
        jobjForFlag.put("Flag",thisFlag );
         arrForFlag.put(jobjForFlag);

        jobjForFlag = new JSONObject();
        jobjForFlag.put("flagdata", arrForFlag);

        String stringFLAGToSend = jobjForFlag.toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Map<String, Object> jsonMap = new Gson().fromJson(stringFLAGToSend, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        Task<Void> myRef = database.getReference().child("dbflag").updateChildren(jsonMap);

    }
    public static String flagToRetun ="";
    public String loadFlag(final String passedUID0){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("dbflag").child("flagdata");

        myRef.addChildEventListener(new ChildEventListener() {



            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String uID = dataSnapshot.child("UID").getValue(String.class);
                String flag_ = dataSnapshot.child("Flag").getValue(String.class);

                if(passedUID0.equals(uID)){
                  flagToRetun = flag_;
                }
            } //end of loop

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return flagToRetun;

    }




}