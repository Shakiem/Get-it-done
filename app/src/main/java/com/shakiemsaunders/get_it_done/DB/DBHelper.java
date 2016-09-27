package com.shakiemsaunders.get_it_done.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;

    public static final String DATABASE_NAME = "TODO_DB.db";

    public static final String TO_DO_TABLE = "TO_DO";
    public static final String TO_DO_ID_COLUMN = "ID";
    public static final String TO_DO_TASK_COLUMN = "TASK";
    public static final String TO_DO_DUE_DATE_COLUMN = "DUE_DATE";
    public static final String TO_DO_PRIORITY_COLUMN = "PRIORITY";
    public static final String TO_DO_STATE_COLUMN = "STATE";
    public static final String TO_DO_COMMENTS_COLUMN = "COMMENTS";
    private final String CREATE_TO_DO_TABLE = "CREATE TABLE IF NOT EXISTS " + TO_DO_TABLE +
            " (" + TO_DO_ID_COLUMN + " INTEGER PRIMARY KEY, " + TO_DO_TASK_COLUMN + " TEXT NOT NULL, " +
            TO_DO_DUE_DATE_COLUMN + " INTEGER NOT NULL, " + TO_DO_PRIORITY_COLUMN + " INTEGER NOT NULL, " +
            TO_DO_STATE_COLUMN + " INTEGER NOT NULL, " + TO_DO_COMMENTS_COLUMN + " TEXT)";

    public static DBHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private DBHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TO_DO_TABLE);
        insertSampleItem(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TO_DO_TABLE);
        onCreate(db);
    }

    private void insertSampleItem(SQLiteDatabase db){
        final int TWO_DAYS = 172800000;
        long dueDate = new Date().getTime() + TWO_DAYS;
        ContentValues values = new ContentValues();
        values.put(TO_DO_TASK_COLUMN, "Take out trash");
        values.put(TO_DO_DUE_DATE_COLUMN, dueDate);
        values.put(TO_DO_PRIORITY_COLUMN, 2);
        values.put(TO_DO_STATE_COLUMN, 0);
        values.put(TO_DO_COMMENTS_COLUMN, "Mom will nag me to death if I don't do this before the garbage truck comes.");

        db.insert(TO_DO_TABLE,null,values);

        values = new ContentValues();
        values.put(TO_DO_TASK_COLUMN, "Finish American Literature Readings");
        values.put(TO_DO_DUE_DATE_COLUMN, (dueDate + TWO_DAYS));
        values.put(TO_DO_PRIORITY_COLUMN, 1);
        values.put(TO_DO_STATE_COLUMN, 0);
        values.put(TO_DO_COMMENTS_COLUMN, "I won't graduate if I do not get an A on the final!");

        db.insert(TO_DO_TABLE,null,values);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        try {
            dueDate = dateFormat.parse("09/30/2016 23:59").getTime();
        } catch (ParseException e) {
            return;
        }
        values = new ContentValues();
        values.put(TO_DO_TASK_COLUMN, "Complete and submit application to CodePath");
        values.put(TO_DO_DUE_DATE_COLUMN, dueDate);
        values.put(TO_DO_PRIORITY_COLUMN, 0);
        values.put(TO_DO_STATE_COLUMN, 1);
        values.put(TO_DO_COMMENTS_COLUMN, "This includes the pre-work. Create that to-do list app" +
                " and make an item to complete the and sumbit the app!");

        db.insert(TO_DO_TABLE,null,values);
    }
}
