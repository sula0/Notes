package com.example.emanuel.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteSQLHelper extends SQLiteOpenHelper {

    private static NoteSQLHelper dbHelper;
    private static final String TABLE = "notes";
    public static final String ROW_ID = "_id";
    public static final String NOTETEXT= "NOTETEXT";
    public static final String DATECREATED = "DATECREATED";
    public static final String TIMECREATED = "TIMECREATED";

    public static synchronized NoteSQLHelper getInstance(Context context) {
        if(dbHelper == null) {
            dbHelper = new NoteSQLHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    private NoteSQLHelper(Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes ("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTETEXT + " TEXT,"
                + DATECREATED + " TEXT,"
                + TIMECREATED + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues noteValues = new ContentValues();
        noteValues.put(NOTETEXT, note.getText());
        noteValues.put(DATECREATED, note.getDateCreated());
        noteValues.put(TIMECREATED, note.getTimeCreated());
        db.insert(TABLE, null, noteValues);
        db.close();
    }

    public void updateNote(long row_id ,Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues noteValues = new ContentValues();
        noteValues.put(NOTETEXT, note.getText());
        noteValues.put(DATECREATED, note.getDateCreated());
        noteValues.put(TIMECREATED, note.getTimeCreated());
        db.update(TABLE, noteValues, (ROW_ID + "=" + row_id), null);
        db.close();
    }

    public void deleteNote(long row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,
                ROW_ID + "= ?",
                new String[]{Long.toString(row_id)});

    }

    public Cursor getNoteAtId(int noteId, SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE,
                new String[]{ROW_ID, NOTETEXT, DATECREATED, TIMECREATED},
                ROW_ID + " = ?",
                new String[] {Integer.toString(noteId)},
                null, null, null
        );
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllNotes(SQLiteDatabase db) {
        return db.query(
                TABLE,
                new String[]{ROW_ID, NOTETEXT, DATECREATED, TIMECREATED},
                null, null, null, null, null);
    }

    // here for testing purposes
    public List<Note> listNotes() {
        List<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("notes",
                new String[]{"NOTETEXT", "DATECREATED", "TIMECREATED"},
                null, null, null, null, null);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Note note = new Note();
            note.setText(cursor.getString(0));
            note.setDateCreated(cursor.getString(1));
            note.setTimeCreated(cursor.getString(2));
            notesList.add(note);
        }
        cursor.close();
        db.close();
        return notesList;
    }


    public void deleteDb(Context context) {
        context.deleteDatabase("notes.db");
    }
}
