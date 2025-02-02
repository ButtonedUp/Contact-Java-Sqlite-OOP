package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Student.db";
    private static final String TABLE_NAME = "Student_details";
    private static final String ID = "id";
    private static final String NAME = "Name";
    private static final String PHONE = "Phone";
    private static final String EMAIL = "Email";
    private static final int VERSION_NUMBER = 1;

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NAME + " TEXT, "
                    + PHONE + " TEXT, "
                    + EMAIL + " TEXT)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private Context context;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
        }
    }

    public long insertData(String name, String phone, String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PHONE, phone);
        contentValues.put(EMAIL, email);

        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        return rowId;
    }

    public Cursor displayAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery(SELECT_ALL, null);
    }

    public boolean updateData(String id, String name, String phone, String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(NAME, name);
        contentValues.put(PHONE, phone);
        contentValues.put(EMAIL, email);

        sqLiteDatabase.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "id = ?", new String[]{id});
    }
    public void deleteAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME); // Delete all rows
        sqLiteDatabase.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "'"); // Reset auto-increment
        sqLiteDatabase.close();
    }

}
