package ru.startandroid.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database {

    private static final String DB_NAME = "notesDatabase5";
    private static final int DB_VERSION = 2;
    private static final String DB_TABLE = "notesTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    private static final String DB_CREATE = "create table " + DB_TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " + COLUMN_TITLE + " text," +
            COLUMN_TEXT + " text," + COLUMN_DATE + " text" + ");";

    private final Context mCtx;

    private DBHelper myDBHelper;
    private SQLiteDatabase myDB;


    public Database(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void open() {
        myDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        myDB = myDBHelper.getWritableDatabase();
    }
    public void close() {
        if (myDBHelper != null) myDBHelper.close();
    }

    public Cursor getAllData() {
        return myDB.query(DB_TABLE, null, null, null, null, null, "_id DESC");
    }
    public void addRec(String title, String text, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_DATE, "Заметка создана: " + date);
        myDB.insert(DB_TABLE, null, cv);
    }
    public void updateRec(long id, String title, String text, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TEXT, text);
        cv.put(COLUMN_DATE, "Последнее изменение: " + date);
        myDB.update(DB_TABLE, cv, COLUMN_ID + " = " + id, null);

    }
    public void delRec(long id) {
        myDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.execSQL("alter table notesTable add column title");
            }
        }
    }
}
