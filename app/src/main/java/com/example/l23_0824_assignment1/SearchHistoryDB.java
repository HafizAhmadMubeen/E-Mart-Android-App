package com.example.l23_0824_assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SearchHistoryDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SearchHistory.db";
    public static final String TABLE_NAME = "history_table";
    public static final String COL_ID = "ID";
    public static final String COL_QUERY = "QUERY";

    public SearchHistoryDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUERY TEXT UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addHistory(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_QUERY, query);
        // Using replace to avoid duplicates and keep recent ones
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<String> getAllHistory() {
        ArrayList<String> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " order by ID desc", null);
        while (res.moveToNext()) {
            history.add(res.getString(1));
        }
        res.close();
        return history;
    }

    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}