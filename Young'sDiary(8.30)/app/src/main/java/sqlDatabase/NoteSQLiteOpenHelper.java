package sqlDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 8/17/2015.
 */
public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {
    public NoteSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table note (noteID INTEGER primary key autoincrement,"
                + " userID TEXT, text TEXT, date TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists note";
        db.execSQL(query);

        onCreate(db);
    }
}
