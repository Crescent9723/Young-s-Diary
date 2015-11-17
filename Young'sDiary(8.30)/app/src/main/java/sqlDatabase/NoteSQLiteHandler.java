package sqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nerdsnulls.youngsdiary.CommonMethod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import data.EventData;
import data.NoteData;

/**
 * Created by Justin on 8/17/2015.
 */
public class NoteSQLiteHandler {
    NoteSQLiteOpenHelper helper;
    SQLiteDatabase db;

    public NoteSQLiteHandler(Context ctx){
        helper = new NoteSQLiteOpenHelper(ctx, "note.sqlite", null, 1);
    }

    public static NoteSQLiteHandler open(Context ctx){
        return new NoteSQLiteHandler(ctx);
    }

    public void close(){
       helper.close();
    }

    public void insert(String text, Date date, String userID){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID", userID);
        values.put("text", text);
        values.put("date", CommonMethod.getInstance().getDateToString(date));
        db.insert("note", null, values);
    }

    public void update(int noteID, String text, Date date, String userID){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID", userID);
        values.put("text", text);
        values.put("date", CommonMethod.getInstance().getDateToString(date));

        db.update("note", values, "noteID = ?", new String[]{Integer.toString(noteID)});
    }

    public void delete(int noteID){
        db = helper.getWritableDatabase();
        db.delete("note", "noteID = ?", new String[]{Integer.toString(noteID)});
    }

    public void clearTable(){
        db = helper.getWritableDatabase();
        db.delete("note", null, null);
    }

    public NoteData selectNoteByDate(String userID, Date date) throws ParseException {
        db = helper.getReadableDatabase();
        Calendar start = CommonMethod.getInstance().setDefaultDate(date, 0, 0);
        Calendar end = CommonMethod.getInstance().setDefaultDate(date, 23, 59);

        String query = "SELECT noteID, text, date from note where userID = '" + userID + "' AND date >= Datetime('" + CommonMethod.getInstance().getDateToString(start.getTime()) + "')" +
                "AND date <= Datetime('" + CommonMethod.getInstance().getDateToString(end.getTime()) + "');";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0){
            insert("", date, userID);
            cursor = db.rawQuery(query, null);
        }
        cursor.moveToNext();

        return new NoteData(cursor.getInt(cursor.getColumnIndex("noteID")), cursor.getString(cursor.getColumnIndex("text")),
                CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("date"))));
    }




}
