package sqlDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 8/17/2015.
 */
public class EventSQLiteOpenHelper extends SQLiteOpenHelper {
    public EventSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table event (eventID INTEGER primary key autoincrement,"
                + " userID TEXT, title TEXT, startDate TEXT, endDate TEXT, startTime TEXT, endTime TEXT, " +
                "description TEXT, tag TEXT, icon INTEGER, repeatType TEXT, duration INTEGER, weekdayList TEXT," +
                "dayList TEXT, month INTEGER, day INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists event";
        db.execSQL(query);

        onCreate(db);
    }
}