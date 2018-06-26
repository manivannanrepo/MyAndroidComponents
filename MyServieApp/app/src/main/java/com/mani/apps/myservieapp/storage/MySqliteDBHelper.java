package com.mani.apps.myservieapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mani.apps.myservieapp.storage.MyServiceContract.TodoEntry.TABLE_NAME;

public class MySqliteDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myservice.db";
    private static final int DB_VERSION = 1;


    public MySqliteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_NAME + " (" +

                /*
                 * TodoEntry did not explicitly declare a column called "_ID". However,
                 * TodoEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                MyServiceContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                MyServiceContract.TodoEntry.COLUMN_DATE + " INTEGER NOT NULL, " +

                MyServiceContract.TodoEntry.COLUMN_TASK + " TEXT NOT NULL," +

                MyServiceContract.TodoEntry.COLUMN_STATUS + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE_TODO);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public long insert(ContentValues contentValues) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.insert(TABLE_NAME, "", contentValues);

    }


}
