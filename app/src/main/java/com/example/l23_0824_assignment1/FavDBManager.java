package com.example.l23_0824_assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class FavDBManager {
    public static final String DATABASE_NAME = "FavouritesDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "fav_products";

    public static final String COLUMN_ID = "id"; // Local SQL ID
    public static final String COLUMN_P_ID = "product_id"; // Firebase ID
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "product_price";
    public static final String COLUMN_DESC = "product_desc";
    public static final String COLUMN_IMG = "product_img";

    Context context;
    DBHelper helper;

    public FavDBManager(Context context) {
        this.context = context;
    }

    public void open() {
        helper = new DBHelper(context);
    }

    public void close() {
        helper.close();
    }

    // CREATE: Add to Favourites
    public long addFavourite(Product product) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_P_ID, product.getId());
        cv.put(COLUMN_NAME, product.getName());
        cv.put(COLUMN_PRICE, product.getPrice());
        cv.put(COLUMN_DESC, product.getDescription());
        cv.put(COLUMN_IMG, product.getImageRes());

        long count = db.insert(TABLE_NAME, null, cv);
        db.close();
        return count;
    }

    // READ: Get all Favourites
    public ArrayList<Product> getAllFavourites() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Product> favs = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int idxPid = cursor.getColumnIndex(COLUMN_P_ID);
            int idxName = cursor.getColumnIndex(COLUMN_NAME);
            int idxPrice = cursor.getColumnIndex(COLUMN_PRICE);
            int idxDesc = cursor.getColumnIndex(COLUMN_DESC);
            int idxImg = cursor.getColumnIndex(COLUMN_IMG);

            do {
                Product p = new Product(
                        cursor.getString(idxPid),
                        cursor.getString(idxName),
                        cursor.getString(idxPrice),
                        cursor.getString(idxDesc),
                        "General", "Unknown"
                );
                // If you had a setImgRes method, you'd use it here
                favs.add(p);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return favs;
    }

    // DELETE: Remove by Firebase ID
    public int deleteFavourite(String productId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, COLUMN_P_ID + "=?", new String[]{productId});
        db.close();
        return count;
    }

    // Check if exists
    public boolean isFavourite(String productId) {
        // Safety Check: If ID is null, it can't be a favourite
        if (productId == null) {
            return false;
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_P_ID + "=?",
                new String[]{productId}, null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return exists;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_P_ID + " TEXT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PRICE + " TEXT, "
                    + COLUMN_DESC + " TEXT, "
                    + COLUMN_IMG + " INTEGER)";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}