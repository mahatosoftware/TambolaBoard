package in.mahato.tambola;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tambola.db";
    private static final int DB_VERSION = 2;


    public DBHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + GameContract.TABLE_CALLED + " ("
                + GameContract.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GameContract.Columns.GAME_ID + " INTEGER,"
                + GameContract.Columns.NUMBER + " INTEGER,"
                + GameContract.Columns.ORDER_INDEX + " INTEGER,"
                + GameContract.Columns.TS + " INTEGER"
                + ");";
        db.execSQL(create);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + GameContract.TABLE_CALLED);
        onCreate(db);
    }
}