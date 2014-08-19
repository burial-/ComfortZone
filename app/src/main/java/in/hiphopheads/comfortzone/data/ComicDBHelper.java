package in.hiphopheads.comfortzone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.hiphopheads.comfortzone.data.ComicContract.ComicEntry;

/**
 * Created on 18/08/14.
 */
public class ComicDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "comics.db";

    public ComicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_XKCD_TABLE = "CREATE TABLE " + ComicEntry.TABLE_NAME + " (" +
                ComicEntry._ID + " INTEGER PRIMARY KEY, " +
                ComicEntry.COLUMN_COMIC_NUM + " REAL, " +
                ComicEntry.COLUMN_COMIC_DATE + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_COMIC_TITLE + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_COMIC_IMG + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_COMIC_ALT_TEXT + " TEXT, " +
                ComicEntry.COLUMN_COMIC_LINK + " TEXT UNIQUE NOT NULL, " +
                ComicEntry.COLUMN_COMIC_AUTHOR + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_COMIC_NAME + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_COMIC_HIDDEN_IMG + " TEXT, " +
                "UNIQUE (" + ComicEntry.COLUMN_COMIC_LINK +") ON CONFLICT IGNORE" +
                " );";
        // Create the table eh
        sqLiteDatabase.execSQL(SQL_CREATE_XKCD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ComicEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}