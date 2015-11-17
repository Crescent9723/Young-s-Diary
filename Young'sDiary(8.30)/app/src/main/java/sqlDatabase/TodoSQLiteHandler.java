package sqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nerdsnulls.youngsdiary.CommonMethod;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import data.EventData;
import data.NoteData;
import data.TodoData;

/**
 * Created by Justin on 8/17/2015.
 */
public class TodoSQLiteHandler {
    TodoSQLiteOpenHelper helper;
    SQLiteDatabase db;

    public TodoSQLiteHandler(Context ctx){
        helper = new TodoSQLiteOpenHelper(ctx, "todo.sqlite", null, 1);
    }

    public static TodoSQLiteHandler open(Context ctx){
        return new TodoSQLiteHandler(ctx);
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
        db.insert("todo", null, values);
    }

    public void update(int todoID, String text, Date date, String userID){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userID", userID);
        values.put("text", text);
        values.put("date", CommonMethod.getInstance().getDateToString(date));

        db.update("todo", values, "todoID = ?", new String[]{Integer.toString(todoID)});
    }

    public void delete(int todoID){
        db = helper.getWritableDatabase();
        db.delete("todo", "todoID = ?", new String[]{Integer.toString(todoID)});
    }

    public void clearTable(){
        db = helper.getWritableDatabase();
        db.delete("todo", null, null);
    }

    public ArrayList<TodoData> selectTodoByDate(String userID, Date date) throws ParseException {
        db = helper.getReadableDatabase();
        Calendar start = CommonMethod.getInstance().setDefaultDate(date, 0, 0);
        Calendar end = CommonMethod.getInstance().setDefaultDate(date, 23, 59);

        String query = "SELECT todoID, text, date from todo where userID = '" + userID + "' AND date >= Datetime('" + CommonMethod.getInstance().getDateToString(start.getTime()) + "')" +
                "AND date <= Datetime('" + CommonMethod.getInstance().getDateToString(end.getTime()) + "');";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<TodoData> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(new TodoData(cursor.getInt(cursor.getColumnIndex("todoID")), cursor.getString(cursor.getColumnIndex("text")),
                CommonMethod.getInstance().getStringToDate(cursor.getString(cursor.getColumnIndex("date")))));
        }
        return list;
    }
}
