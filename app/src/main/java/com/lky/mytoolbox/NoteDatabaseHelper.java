package com.lky.mytoolbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";
    private static final String COLUMN_REMINDER_DATE = "reminder_date";
    private static final String COLUMN_ALARM_TIME = "alarm_time";
    private static final String COLUMN_DRAWING_DATA = "drawing_data";
    private static final String COLUMN_PHOTO_DATA = "photo_data";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_IS_FAVORITE + " INTEGER,"
                + COLUMN_REMINDER_DATE + " TEXT,"
                + COLUMN_ALARM_TIME + " TEXT,"
                + COLUMN_DRAWING_DATA + " BLOB,"
                + COLUMN_PHOTO_DATA + " BLOB"
                + ")";
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_DATE, note.getDate().getTime());
        values.put(COLUMN_IS_FAVORITE, note.isFavorite() ? 1 : 0);
        values.put(COLUMN_REMINDER_DATE, note.getReminderDate());
        values.put(COLUMN_ALARM_TIME, note.getAlarmTime());
        values.put(COLUMN_DRAWING_DATA, note.getDrawingData());
        values.put(COLUMN_PHOTO_DATA, note.getPhotoData());
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_DATE, note.getDate().getTime());
        values.put(COLUMN_IS_FAVORITE, note.isFavorite() ? 1 : 0);
        values.put(COLUMN_REMINDER_DATE, note.getReminderDate());
        values.put(COLUMN_ALARM_TIME, note.getAlarmTime());
        values.put(COLUMN_DRAWING_DATA, note.getDrawingData());
        values.put(COLUMN_PHOTO_DATA, note.getPhotoData());
        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        new Date(cursor.getLong(3)),
                        cursor.getInt(4) == 1,
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getBlob(7),
                        cursor.getBlob(8)
                );
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return noteList;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{
                COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_DATE, COLUMN_IS_FAVORITE,
                COLUMN_REMINDER_DATE, COLUMN_ALARM_TIME, COLUMN_DRAWING_DATA, COLUMN_PHOTO_DATA
        }, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Note note = new Note(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    new Date(cursor.getLong(3)),
                    cursor.getInt(4) == 1,
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getBlob(7),
                    cursor.getBlob(8)
            );
            cursor.close();
            return note;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
}
