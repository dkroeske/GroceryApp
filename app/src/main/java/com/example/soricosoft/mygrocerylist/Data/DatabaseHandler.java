package com.example.soricosoft.mygrocerylist.Data;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import com.example.soricosoft.mygrocerylist.Model.Grocery;
import com.example.soricosoft.mygrocerylist.Util.Constants;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul de Mast on 26-Oct-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context contect;
    private final String TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.contect = context;
    }

    // Aangeroepen bij de eerste aanroep van getWritableDatabase / getReadableDatabase:
    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Grocery_Table = "CREATE TABLE " + Constants.TABLE_NAME
                + " (" + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.KEY_GROCERY_ITEM + " TEXT, "
                + Constants.KEY_QTY_NUMBER + " TEXT, "
                + Constants.KEY_DATE_NAME + " TEXT)";

        db.execSQL(Create_Grocery_Table);

        Log.i(TAG, "onCreate: " + Create_Grocery_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD operations
     * Create, Read, Update, Delete Methods
     */
    public long addGroceryItem(Grocery grocery) {

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, LocalDateTime.now().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        long row = db.insert(Constants.TABLE_NAME, null, values);
        Log.d(TAG, "addGroceryItem: inserted " + grocery.toString() + " at row " + row);
        return row;
    }

    public Grocery getGrocery(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Grocery grocery = new Grocery();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                        Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + " =? ", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            grocery.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
            grocery.setDateItemAdded(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_NAME))));
        }

        return grocery;
    }


    public List<Grocery> getGroceries() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Grocery> groceries = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                        Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Grocery grocery = new Grocery();
                grocery.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                grocery.setDateItemAdded(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_NAME))));

                groceries.add(grocery);
            } while (cursor.moveToNext());
        }

        return groceries;
    }

    public int updateGrocery(Grocery grocery) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, grocery.getDateItemAdded().toString());

        // update row:
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "= ?", new String[]{String.valueOf(grocery.getId())});

    }

    public void deleteGrocery(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getGroceriesCount() {
        String CountQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(CountQuery, null);

        return cursor.getCount();
    }
}
