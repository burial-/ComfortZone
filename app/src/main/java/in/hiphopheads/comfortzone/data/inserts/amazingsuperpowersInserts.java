package in.hiphopheads.comfortzone.data.inserts;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;
import java.util.Vector;

import in.hiphopheads.comfortzone.R;
import in.hiphopheads.comfortzone.data.ComicContract;
import in.hiphopheads.comfortzone.data.comic.interfaces.IComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.amazingsuperpowersComicInfo;

/**
 * Created on 19/08/14.
 */
public class amazingsuperpowersInserts {

    // Basic insert function receives a list of comics and then inserts them into the database

    Context mContext;

    public amazingsuperpowersInserts(Context context) {
        mContext = context;
    }

    // takes a bulk list of comics and the size then runs through them all
    public void bulkInsertAmazingsuperpowersDatabase(List<amazingsuperpowersComicInfo> comics) {
        Vector<ContentValues> cVVector = new Vector<ContentValues>(comics.size());

        for (IComicInfo comic : comics) {
            ContentValues comicValues = new ContentValues();

            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_DATE, comic.getPostDate());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_TITLE, comic.getTitle());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_IMG, comic.getImageUrl());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_ALT_TEXT, comic.getAltText());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_HIDDEN_IMG, comic.getHiddenImageUrl());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_LINK, comic.getUrl());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_AUTHOR, comic.getAuthor());
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_NAME, mContext.getString(R.string.amazingsuperpowers));

            cVVector.add(comicValues);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(ComicContract.ComicEntry.CONTENT_URI, cvArray);
        }
    }
}
