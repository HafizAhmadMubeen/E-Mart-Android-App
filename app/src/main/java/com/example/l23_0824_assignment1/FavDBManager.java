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
    public static final int DATABASE_VERSION = 2; // ✅ bumped
    public static final String TABLE_NAME = "fav_products";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_P_ID = "product_id";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "product_price";
    public static final String COLUMN_DESC = "product_desc";
    public static final String COLUMN_IMG = "product_img";
    public static final String COLUMN_SELLER_ID = "seller_id"; // ✅ NEW

    Context context;
    DBHelper helper;

    public FavDBManager(Context context) {
        this.context = context;
    }

    public void open() { helper = new DBHelper(context); }
    public void close() { helper.close(); }

    public long addFavourite(Product product) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_P_ID, product.getId());
        cv.put(COLUMN_NAME, product.getName());
        cv.put(COLUMN_PRICE, product.getPrice());
        cv.put(COLUMN_DESC, product.getDescription());
        cv.put(COLUMN_IMG, product.getImageRes());
        cv.put(COLUMN_SELLER_ID, product.getSellerId());
        long count = db.insert(TABLE_NAME, null, cv);
        db.close();
        return count;
    }

    public ArrayList<Product> getAllFavourites() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Product> favs = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int idxPid = cursor.getColumnIndex(COLUMN_P_ID);
            int idxName = cursor.getColumnIndex(COLUMN_NAME);
            int idxPrice = cursor.getColumnIndex(COLUMN_PRICE);
            int idxDesc = cursor.getColumnIndex(COLUMN_DESC);
            int idxSellerId = cursor.getColumnIndex(COLUMN_SELLER_ID);

            do {
                Product p = new Product(
                        cursor.getString(idxPid),
                        cursor.getString(idxName),
                        cursor.getString(idxPrice),
                        cursor.getString(idxDesc),
                        "General",
                        cursor.getString(idxSellerId)
                );
                favs.add(p);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return favs;
    }

    public int deleteFavourite(String productId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, COLUMN_P_ID + "=?", new String[]{productId});
        db.close();
        return count;
    }

    public boolean isFavourite(String productId) {
        if (productId == null) return false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_P_ID + "=?",
                new String[]{productId}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_P_ID + " TEXT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PRICE + " TEXT, "
                    + COLUMN_DESC + " TEXT, "
                    + COLUMN_IMG + " INTEGER, "
                    + COLUMN_SELLER_ID + " TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}