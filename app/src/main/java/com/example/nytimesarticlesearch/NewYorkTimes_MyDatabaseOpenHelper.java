package com.example.nytimesarticlesearch;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class NewYorkTimes_MyDatabaseOpenHelper is for the database helper, it extends SQLiteOpenHelper.
 */

public class NewYorkTimes_MyDatabaseOpenHelper extends SQLiteOpenHelper {
    /**
     * DATABASE_NAME variable is the name of the database
     * VERSION_NUM variable is the database version number
     * TABLE_NAME variable is the name of the table
     * COL_ID is the column id
     * COL_HEADER is the header (title) of the article
     * COL_URL variable is the article url
     * COL_PIC_URL is the picture url of the article
     */
    public static final String DATABASE_NAME = "MyDatabase";
    public static final int VERSION_NUM = 9;
    public static final String TABLE_NAME = "ArticlesTable";
    public static final String COL_ID = "_id";
    public static final String COL_HEADER = "ARTICLE_HEADER";
    public static final String COL_URL = "ARTICLE_URL";
    public static final String COL_PIC_URL = "PICTURE_URL";

    public NewYorkTimes_MyDatabaseOpenHelper(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * This initiate the fields and make sure you put spaces between SQL statements and Java strings .
     */
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_HEADER + " TEXT, " + COL_URL + " TEXT, " + COL_PIC_URL + " TEXT)");
    }

    /**
     * This method is used to delete the old table and create new one onUpgrade.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * This method is used to delete the old table and create new one onDowngrade .
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
