package sqlDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 8/17/2015.
 */
public class TodoSQLiteOpenHelper extends SQLiteOpenHelper {
    public TodoSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table todo (todoID INTEGER primary key autoincrement,"
                + " userID TEXT, text TEXT, date TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists todo";
        db.execSQL(query);

        onCreate(db);
    }
}
