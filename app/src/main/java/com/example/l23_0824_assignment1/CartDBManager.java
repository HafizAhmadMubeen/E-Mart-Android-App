package com.example.l23_0824_assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CartDBManager {
    public static final String DATABASE_NAME = "CartDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "cart";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_P_ID = "product_id";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "product_price";
    public static final String COLUMN_DESC = "product_desc";
    public static final String COLUMN_QTY = "quantity";

    Context context;
    DBHelper helper;

    public CartDBManager(Context context) { this.context = context; }

    public void open() { helper = new DBHelper(context); }
    public void close() { if (helper != null) helper.close(); }

    // Requirement 1 & 2: Add/Insert with Quantity
    public long addToCart(Product product) {
        SQLiteDatabase db = helper.getWritableDatabase();

        // Check if exists to increment instead of insert
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_P_ID + "=?", new String[]{product.getId()}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndex(COLUMN_QTY));
            updateQuantity(product.getId(), currentQty + 1);
            cursor.close();
            return 1;
        }

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_P_ID, product.getId());
        cv.put(COLUMN_NAME, product.getName());
        cv.put(COLUMN_PRICE, product.getPrice());
        cv.put(COLUMN_DESC, product.getDescription());
        cv.put(COLUMN_QTY, 1);
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result;
    }

    // Requirement 3: Update Query for quantity
    public void updateQuantity(String productId, int newQty) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QTY, newQty);
        db.update(TABLE_NAME, cv, COLUMN_P_ID + "=?", new String[]{productId});
        db.close();
    }

    // Requirement 4 & 6: Fetch latest data
    public ArrayList<CartItem> getAllCartItems() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<CartItem> items = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getString(cursor.getColumnIndex(COLUMN_P_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESC)),
                        "General", "Unknown"
                );
                int qty = cursor.getInt(cursor.getColumnIndex(COLUMN_QTY));
                items.add(new CartItem(p, qty));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return items;
    }

    // Requirement 5: Delete Query
    public void deleteItem(String productId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_P_ID + "=?", new String[]{productId});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_P_ID + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_QTY + " INTEGER)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}