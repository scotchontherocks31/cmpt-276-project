package ca.sfu.cmpt_276_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.sfu.cmpt_276_project.Model.InspectionData;
import ca.sfu.cmpt_276_project.Model.Restaurant;


// TO USE:
// Change the package (at top) to match your project.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    public static final String KEY_TRACK_NUM = "trackNumber";
    public static final String KEY_RES_NAME = "restaurantName";
    public static final String KEY_ADDRESS = "physicalAddress";
    public static final String KEY_CITY = "physicalCity";
    public static final String KEY_FAC_TYPE = "facType";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ICON = "icon";
    public static final String KEY_INSPECTION = "inspection";
    public static final String KEY_ARRLIST_NUM = "arrlistNumber";

    public static final int COL_TRACK_NUM = 1;
    public static final int COL_RES_NAME  = 2;
    public static final int COL_ADDRESS = 3;
    public static final int COL_CITY = 4;
    public static final int COL_FAC_TYPE = 5;
    public static final int COL_LATITUDE = 6;
    public static final int COL_LONGITUDE = 7;
    public static final int COL_ICON = 8;
    public static final int COL_INSPECTION = 9;
    public static final int COL_ARRLIST_NUM = 10;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TRACK_NUM, KEY_RES_NAME,
            KEY_ADDRESS, KEY_CITY, KEY_FAC_TYPE, KEY_LATITUDE, KEY_LONGITUDE, KEY_ICON,
            KEY_INSPECTION, KEY_ARRLIST_NUM};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;
    Gson gson = new Gson();

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

                    /*
                     * CHANGE 2:
                     */
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_TRACK_NUM + " text not null, "
                    + KEY_RES_NAME + " text not null, "
                    + KEY_ADDRESS + " text not null, "
                    + KEY_CITY + " text not null, "
                    + KEY_FAC_TYPE + " text not null, "
                    + KEY_LATITUDE + " real not null, "
                    + KEY_LONGITUDE + " real not null, "
                    + KEY_ICON + " integer not null,"
                    + KEY_INSPECTION + " text not null, "
                    + KEY_ARRLIST_NUM + " text not null"

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private static DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = DatabaseHelper.getHelper(ctx);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    public int count(){
        return this.getAllRows().getCount();
    }

    public Restaurant getRestaurant(int rowNum){
        int rowCount = 0;
        Restaurant restaurant = new Restaurant();
        Cursor cursor = getAllRows();
        if(cursor.moveToFirst()){
            //search for row to add restaurant
            while(rowCount < rowNum){
                rowCount++;
                cursor.moveToNext();
            }
            Type type = new TypeToken<ArrayList<InspectionData>>() {}.getType();
            List<InspectionData> tempList =
                    gson.fromJson(cursor.getString(DBAdapter.COL_INSPECTION), type);

            restaurant.setTrackNumber(cursor.getString(DBAdapter.COL_TRACK_NUM));
            restaurant.setRestaurantName(cursor.getString(DBAdapter.COL_RES_NAME));
            restaurant.setPhysicalAddress(cursor.getString(DBAdapter.COL_ADDRESS));
            restaurant.setPhysicalCity(cursor.getString(DBAdapter.COL_CITY));
            restaurant.setFacType(cursor.getString(DBAdapter.COL_FAC_TYPE));
            restaurant.setLatitude(cursor.getDouble(DBAdapter.COL_LATITUDE));
            restaurant.setLongitude(cursor.getDouble(DBAdapter.COL_LONGITUDE));
            restaurant.setInspectionDataList(tempList);
            restaurant.setIcon(cursor.getInt(DBAdapter.COL_ICON));
        }
        cursor.close();
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants(){
        List<Restaurant> restaurants = new ArrayList<>();

        Cursor cursor = this.getAllRows();
        if(cursor.moveToFirst()){
            do{
                //temp restaurant container
                Restaurant restaurant = new Restaurant();

                //THIS PAIR OF LINES ARE USED TO DESERIALIZE THE JSON STRING EXTRACTED FROM DB
                Type type = new TypeToken<ArrayList<InspectionData>>() {}.getType();
                List<InspectionData> tempList =
                        gson.fromJson(cursor.getString(DBAdapter.COL_INSPECTION), type);

                restaurant.setTrackNumber(cursor.getString(DBAdapter.COL_TRACK_NUM));
                restaurant.setRestaurantName(cursor.getString(DBAdapter.COL_RES_NAME));
                restaurant.setPhysicalAddress(cursor.getString(DBAdapter.COL_ADDRESS));
                restaurant.setPhysicalCity(cursor.getString(DBAdapter.COL_CITY));
                restaurant.setFacType(cursor.getString(DBAdapter.COL_FAC_TYPE));
                restaurant.setLatitude(cursor.getDouble(DBAdapter.COL_LATITUDE));
                restaurant.setLongitude(cursor.getDouble(DBAdapter.COL_LONGITUDE));
                restaurant.setInspectionDataList(tempList);
                restaurant.setIcon(cursor.getInt(DBAdapter.COL_ICON));

                //add retrieved restaurant into Array List
                restaurants.add(restaurant);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return restaurants;
    }

    public void addRestaurant(Restaurant restaurant, int arrListNum){

        String inspectionJSON = gson.toJson(restaurant.getInspectionDataList());

        //THIS PROCESS ADDS ITEM TO THE DM
        long newID =
                this.insertRow(restaurant.getTrackNumber(),
                        restaurant.getRestaurantName(),
                        restaurant.getPhysicalAddress(),
                        restaurant.getPhysicalCity(),
                        restaurant.getFacType(),
                        restaurant.getLatitude(),
                        restaurant.getLongitude(),
                        restaurant.getIcon(),
                        inspectionJSON,
                        arrListNum);

    }

    public void deleteRestaurant(int arrListNum){

        Cursor cursor = getAllRows();
        if(cursor.moveToFirst()){
            //search for row to delete
            do{
                if(cursor.getInt(DBAdapter.COL_ARRLIST_NUM) == arrListNum){
                    long rowToDelete = cursor.getInt(DBAdapter.COL_ROWID);
                    this.deleteRow(rowToDelete);
                    cursor.close();
                    return;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean checkRestaurant(int arrListNum){

        Cursor cursor = getAllRows();
        if(cursor.moveToFirst()){
            //search for row to delete
            do{
                if(cursor.getInt(DBAdapter.COL_ARRLIST_NUM) == arrListNum){
                    cursor.close();
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    public void print(){
        if(this.getAllRows().getCount() < 1){
            System.out.println("Empty db!`````````````````````````````");
        }

        Cursor cursor = getAllRows();

        if(cursor.moveToFirst()){
            do{
                //THIS PAIR OF LINES ARE USED TO DESERIALIZE THE JSON STRING EXTRACTED FROM DB
                Type type = new TypeToken<ArrayList<InspectionData>>() {}.getType();
                List<InspectionData> tempList = gson.fromJson(cursor.getString(DBAdapter.COL_INSPECTION), type);

                //Printer test to check injection
                System.out.println("DB ENTRIES: \n"
                        + "\tDB-ID#: " + cursor.getInt(DBAdapter.COL_ROWID) + "\n"
                        + "\tTrack#: " + cursor.getString(DBAdapter.COL_TRACK_NUM) + "\n"
                        + "\tName: " + cursor.getString(DBAdapter.COL_RES_NAME) + "\n"
                        + "\tAddr: " + cursor.getString(DBAdapter.COL_ADDRESS) + "\n"
                        + "\tCity: " + cursor.getString(DBAdapter.COL_CITY) + "\n"
                        + "\tFacType: " + cursor.getString(DBAdapter.COL_FAC_TYPE) + "\n"
                        + "\tLatitude: " + cursor.getDouble(DBAdapter.COL_LATITUDE) + "\n"
                        + "\tLongitude: " + cursor.getDouble(DBAdapter.COL_LONGITUDE) + "\n"
                        + "\tArrList#: " + cursor.getDouble(DBAdapter.COL_ARRLIST_NUM) + "\n"
                        + "---------------------------------------------------------------------\n");


            }while (cursor.moveToNext());
        }

        cursor.close();
    }

    public boolean searchDBforRestaurant(Restaurant restaurant){
        Cursor cursor = getAllRows();
        if(cursor.moveToFirst()){
            //search for row to delete
            do{
                if(restaurant.getTrackNumber() == cursor.getString(DBAdapter.COL_TRACK_NUM)){
                    cursor.close();
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    // Add a new set of values to the database.
    public long insertRow(String trackNumber,
            String restaurantName,
            String physicalAddress,
            String physicalCity,
            String facType,
            double latitude,
            double longitude,
            int icon, String inspectionJSON, int arrListNum) {
        /*
         * CHANGE 3:
         */
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRACK_NUM, trackNumber);
        initialValues.put(KEY_RES_NAME, restaurantName);
        initialValues.put(KEY_ADDRESS, physicalAddress);
        initialValues.put(KEY_CITY, physicalCity);
        initialValues.put(KEY_FAC_TYPE, facType);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        initialValues.put(KEY_ICON, icon);
        initialValues.put(KEY_INSPECTION, inspectionJSON);
        initialValues.put(KEY_ARRLIST_NUM, arrListNum);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
//        Cursor c = getAllRows();
//        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
//        int limit = 0;
//
//        while (c.moveToFirst()) {
//            do {
//                deleteRow(c.getLong((int) rowId));
//            } while (c.moveToNext());
//        }
//        c.close();
        myDBHelper = new DBAdapter.DatabaseHelper(context);
        db = myDBHelper.getWritableDatabase();
        db.delete(DATABASE_TABLE,null,null);

        //Resets ID sequence to 0
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DATABASE_TABLE + "'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ = 0 WHERE NAME = '" + DATABASE_TABLE + "'");
        System.out.println("Wiped DB clean");
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId,
                             String trackNumber,
                             String restaurantName,
                             String physicalAddress,
                             String physicalCity,
                             String facType,
                             double latitude,
                             double longitude,
                             int icon,
                             String inspectionJSON,
                             int arrListNum) {
        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
        /**
         * Update data in the row with new fields.
         * Also change the function's arguments to be what you need!
         * */
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TRACK_NUM, trackNumber);
        newValues.put(KEY_RES_NAME, restaurantName);
        newValues.put(KEY_ADDRESS, physicalAddress);
        newValues.put(KEY_CITY, physicalCity);
        newValues.put(KEY_FAC_TYPE, facType);
        newValues.put(KEY_LATITUDE, latitude);
        newValues.put(KEY_LONGITUDE, longitude);
        newValues.put(KEY_ICON, icon);
        newValues.put(KEY_INSPECTION, inspectionJSON);
        newValues.put(KEY_ARRLIST_NUM, arrListNum);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }



    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        private static DatabaseHelper instance;

        public static synchronized DatabaseHelper getHelper(Context context)
        {
            if (instance == null){
                System.out.println("DB instance was null");
                instance = new DatabaseHelper(context.getApplicationContext());
            }

            return instance;
        }

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}