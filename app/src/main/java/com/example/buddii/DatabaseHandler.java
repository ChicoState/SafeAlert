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

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// SQLITE set up to handle DATABASE
public class DatabaseHandler extends SQLiteOpenHelper {
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
    private static final String USER_UID = "Uid";
    private static final String USER_RATINGS = "user_ratings";
    private static final String USER_FLAG = "user_flag";

    /* Prepping for GPS DB */
    private static final String  LATITUDE = "latitude";
    private static final String LONGITUTDE = "longitude";
    private static final String GPS_TABLE = "GPS_TABLE";
    /* GPS DB */

    // temp plz delete
    String jsonString2 ="";
    private final Context context;
    // for hashing
    String shaPword = "";
    String shaPword2 = "";
   public static String shaPwordToCompare = "";
   public static byte[] salt = null;
    public static String jsonToSend = "nothing";
   public static JSONObject usersObj ;
   public static JSONArray arr1 = new JSONArray();




    //Preping Strings for Creation/deletion/altering of Databases
    // Basically a concatination of SQL COMMANDS

    private String CREATE_TABLE = "CREATE TABLE " + NAME_OF_USERS_TABLE + "( Uid INTEGER primary key autoincrement," + USER_PHONE + " PlaceHolder," +
            USER_NAME + " PlaceHolder," + USER_EMAIL + " PlaceHolder," + USER_PASSWORD + " PlaceHolder ," + USER_RATINGS+ "," + USER_SALT +  ")";

    private String CREATE_FRIENDS_TABLE = "CREATE TABLE " + NAME_OF_FRIENDS_TABLE + "( Uid INTEGER)";
    private String CREATE_ACTIVE_BUDDII_TABLE = "CREATE TABLE " + NAME_OF_ACTIVE_BUDDI_TABLE + "( Uid INTEGER)";
    private String CREATE_FLAG_TABLE = "CREATE TABLE " + NAME_OF_FLAG_TABLE + "( Uid INTEGER)";


    private String CREATE_GPS_TABLE = "CREATE TABLE " + GPS_TABLE + "(" + LATITUDE + " PlaceHolder," +
            LONGITUTDE + " PlaceHolder"  + ")";

    //Will Replace table if exist ( replace USER)
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_OF_USERS_TABLE;

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
        My_Database.execSQL(CREATE_ACTIVE_BUDDII_TABLE);
        My_Database.execSQL(CREATE_FLAG_TABLE);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addToDb(String userphone, String name, String email, String password)  {
         salt = hashSha512.getSalt();

        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor c = My_Database.rawQuery("SELECT * FROM USERS_TABLE where user_name = " + " '" + name + "'", null);

        if(c.getCount()>0)
        {
            Toast.makeText(context, "USER ALREADY EXITS", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            Toast.makeText(context, "WELCOME", Toast.LENGTH_LONG).show();
        }

        values.put(DatabaseHandler.USER_PHONE, userphone);
        values.put(DatabaseHandler.USER_NAME, name);
        values.put(DatabaseHandler.USER_EMAIL, email);
        //password will be sent to function and hashed


         shaPword = hashSha512.hashPaswordSHA512(password,salt);
         //prep for storing
         shaPwordToCompare = shaPword.substring(0,128);


        values.put(DatabaseHandler.USER_PASSWORD, shaPword);

        // in order to compare users password, we need to store the original SALT which was used to
        //hash the password. Otherwise a new SALT would be generated and would result in a differnt hash.

        String byteSaltToString = Base64.getEncoder().encodeToString(salt);
        values.put(DatabaseHandler.USER_SALT, byteSaltToString);


        long status = My_Database.insert(NAME_OF_USERS_TABLE, null, values);



        if (status <= 0) {
            //TOAST ...ITS A CELEBRATION
            Toast.makeText(context, "Insertion Unsuccessful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Insertion Successful", Toast.LENGTH_LONG).show();
        }

       // My_Database.close();
      //  c.close();
        setLocalDBToJSON();
        String Jsonxx = mytempJSONreturnFunc();
        //Log.d("xxx",Jsonxx);
        // sending to ONLINE firebase DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String jsonString; //set to json string
        Map<String, Object> jsonMap = new Gson().fromJson(Jsonxx, new TypeToken<HashMap<String, Object>>() {}.getType());
        Task<Void> myRef = database.getReference().child("usersdb").child("users").updateChildren(jsonMap);
    }



    public String checkFireBaseDBForUsers(){

        Log.d("xxxFromRTV","InRTN");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("usersdb").child("users").child("data");

        myRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


               String key = dataSnapshot.getKey();
            //   Log.d("xxxKeyis",key);
               if (key == ""){
                   Log.d("xxxxkeyRTN","KEYretuning");
                   return;
               };

               String uID = dataSnapshot.child("uID").getValue(String.class);
               // Log.d("xxxUIDis",uID);
               if(uID == ""){
                   Log.d("xxxxuIDRTN","UIDretuning");
               }
                String email = dataSnapshot.child("user_email").getValue(String.class);
                if (email == null){
               //     Log.d("xxxxEMAIL","EMAILretuning");
                    return;
                };
                String namex = dataSnapshot.child("user_name").getValue(String.class);
                String pass = dataSnapshot.child("user_pass").getValue(String.class);
                String phone = dataSnapshot.child("user_phone").getValue(String.class);
                String salt = dataSnapshot.child("user_salt").getValue(String.class);
               /*
                Log.d("xxxtEmail",email);
               Log.d("xxxPASSis",pass);
                Log.d("xxxNAMEis",namex);
                Log.d("xxxPHONEis",phone);
                Log.d("xxxSALTis",salt);
                */
                usersObj= new JSONObject();
                try {

                    usersObj.put("uID",uID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usersObj.put("user_phone",phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usersObj.put("user_name",namex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usersObj.put("user_email",email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    usersObj.put("user_pass",pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                try {
                    usersObj.put("user_ratings",);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */

                try {
                    usersObj.put("user_salt",salt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("xxxUSEROBJ",usersObj.toString());
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

        jsonToSend=usersObj.toString();

            dbSYNC(jsonToSend);

        return jsonToSend;
    }

    public void dbSYNC(String jsonString)  {


        try {
            JSONObject jsnobject = new JSONObject(jsonString);
            JSONArray jsonArray = jsnobject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
            }
            for (int i = 0; i < recs.length(); ++i) {
                JSONObject rec = recs.getJSONObject(i);
                int id = rec.getInt("id");
                String loc = rec.getString("loc");
                // ...
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }

/*
        try {

        Log.d("xxxInSYNC",jsonString);
            ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = null;

            jsonArray = new JSONArray(jsonString);


            for (int i = 0; i < jsonArray.length(); i++) {

                    stringArray.add(jsonArray.getString(i));
                 String zz = jsonArray.getString(i);
                Log.d("xxarrList",zz);


            }
            String ccc =stringArray.get(0).toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


 */

       /*
        JSONObject myjson = null;
        try {
            myjson = new JSONObject(String.valueOf(dataToSend));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray the_json_array = myjson.getJSONArray("data");

            int size = the_json_array.length();
            ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
            for (int i = 0; i < size; i++) {
                JSONObject another_json_object = the_json_array.getJSONObject(i);

                arrays.add(another_json_object);
            }

//Finally
            JSONObject[] jsons = new JSONObject[arrays.size()];
            arrays.toArray(jsons);
            Log.d("xxxSYNCarr",the_json_array.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    */


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
            String salt = cursor.getString(6);

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
                    arrayOfIndecesHolder[i] = salt;
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
        long tempTotalrows = DatabaseUtils.queryNumEntries(My_Database,NAME_OF_USERS_TABLE,null);
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
    public Cursor getAllData(String fromThisDB) {

        if (fromThisDB == "userTable")
        {

            SQLiteDatabase My_Database = this.getReadableDatabase();
            String selectQuery = "Select * from "+NAME_OF_USERS_TABLE;
            Cursor cursor = My_Database.rawQuery(selectQuery, null);
         //   cursor.close();
         //   My_Database.close();
            return cursor;
        }
        if (fromThisDB == "friendsTable")
        {
            String selectQuery = "Select * from "+NAME_OF_FRIENDS_TABLE;
            SQLiteDatabase My_Database = this.getReadableDatabase();
            Cursor cursor = My_Database.rawQuery(selectQuery, null);
            cursor.close();
         //   My_Database.close();
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
            try {
                jobj.put("user_ratings",cursor.getString(5));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jobj.put("user_salt",cursor.getString(6));
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


    public String checkCredentials(String checkThisPassword,String pwordInDB){


        String pwordToHash = checkThisPassword;


       // String[] tpassword = loadUsers("user_pass");
       // String tpassword= getPword();

        //String pass = new String(tpassword[0]);

        String shaPwordToCheck = "";

        shaPwordToCheck = hashSha512.hashPaswordSHA512(pwordToHash, salt);


        // for an UNKNOWN reason the strings have to be substrings in order for the
        // comparisons to work
        String t_pwordInDB =pwordInDB.substring(0,128);
        String t_shaPwordToCheck = shaPwordToCheck.substring(0,128);

      //  Log.d("xxxxCCCCCC",t_pwordInDB);
      //  Log.d("xxxxDDDDDD",t_shaPwordToCheck);

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

    // function to populate ACTIVE BUDDI TABLE by adding the Logged in users UID
    //will be called when be a buddii button is pressed
    public  String addToActiveBuddiTable(){
        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String curActiveBuddiLoggedIn = "10";
        values.put(DatabaseHandler.USER_UID, curActiveBuddiLoggedIn);
        My_Database.insert(NAME_OF_ACTIVE_BUDDI_TABLE, null, values);
       return "stuff";
    };

    public void removeFromActiveBuddiTable() {
        String id = "10";

        SQLiteDatabase My_Database = this.getWritableDatabase();
        // delete user record by phone
         My_Database.delete(NAME_OF_ACTIVE_BUDDI_TABLE, USER_UID + " = ?",
                new String[]{String.valueOf(id)});
          My_Database.close();
    }


    // will need Uid and rating as parameter
    public  String addRating(Double rating){

        SQLiteDatabase My_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // do all the average calculations here
        String t_Rating =String.valueOf(rating);
       Integer Uid = 2;
        values.put("user_ratings",t_Rating);
        My_Database.update(NAME_OF_USERS_TABLE,values,"Uid ="+Uid, null);
        return "stuff";
    };


    public boolean chechIfAlreadyMemeber(String username)
    {

        SQLiteDatabase My_Database = this.getWritableDatabase();
         /*
        // delete user record by phone
        My_Database.(NAME_OF_ACTIVE_BUDDI_TABLE, USER_UID + " = ?",
                new String[]{String.valueOf(username)});
        My_Database.close();
        */
      return false;
    };


                                // qwerty
    public String getPword(String pwordFromLogIn) {




        /*
        SQLiteDatabase My_Database;
        My_Database = this.getWritableDatabase();

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

            result= password;
        }
        System.getProperty("line.separator");
        cursor.close();
        My_Database.close();

        */
        String password1 = shaPwordToCompare;
        String checkCred = checkCredentials(pwordFromLogIn, password1);

        return checkCred;
    }







}