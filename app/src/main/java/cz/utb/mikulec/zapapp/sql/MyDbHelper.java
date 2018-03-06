package cz.utb.mikulec.zapapp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiri.mikulec on 01.03.2018.
 */

public class MyDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ZapApp.db";



    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE history (ID INTEGER PRIMARY KEY AUTOINCREMENT, datum DATETIME DEFAULT (CURRENT_TIMESTAMP), IC INTEGER, nazev VARCHAR, ulice VARCHAR, cp INTEGER, psc INTEGER, mesto VARCHAR);";


    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }


}
