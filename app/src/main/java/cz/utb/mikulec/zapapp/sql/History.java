package cz.utb.mikulec.zapapp.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.utb.mikulec.zapapp.App;
import cz.utb.mikulec.zapapp.Firma;

/**
 * Created by jiri.mikulec on 01.03.2018.
 */

public class History {

    public static List<Firma> ITEMS = new ArrayList<>();

    static {

        //Toast.makeText(App.getAppContext(), "History.java", Toast.LENGTH_SHORT).show();

        readData();



    }

    public static void readData() {

        MyDbHelper myDbHelper = new MyDbHelper(App.getAppContext());
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM history ORDER BY ID DESC", null);

        ITEMS.clear();
        Firma firma;
        if (cursor.moveToFirst()) {
            do {
                firma = new Firma();

                firma.setNazev(cursor.getString(cursor.getColumnIndex("nazev")));
                firma.setMesto(cursor.getString(cursor.getColumnIndex("mesto")));
                firma.setIC(cursor.getInt(cursor.getColumnIndex("IC")));

                Log.i("DB nazev", firma.getNazev());
                addItem(firma);

            } while (cursor.moveToNext());
        }

    }



    private static void addItem(Firma firma) {
        ITEMS.add(firma);
        //ITEM_MAP.put(item.id, item);
    }
}
