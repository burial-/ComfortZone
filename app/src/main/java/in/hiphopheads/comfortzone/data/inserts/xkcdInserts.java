package in.hiphopheads.comfortzone.data.inserts;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;
import java.util.Vector;

import in.hiphopheads.comfortzone.R;
import in.hiphopheads.comfortzone.data.comic.interfaces.IComicInfo;
import in.hiphopheads.comfortzone.data.ComicContract.ComicEntry;
import in.hiphopheads.comfortzone.data.comic.interfaces.xkcdComicInfo;

/**
 * Created on 18/08/14.
 */
public class xkcdInserts {

    Context mContext;

    public xkcdInserts(Context context) {
        mContext = context;
    }

    // takes a bulk list of comics and the size then runs through them all
    public void bulkInsertXkcdDatabase(List<xkcdComicInfo> comics) {
        Vector<ContentValues> cVVector = new Vector<ContentValues>(comics.size());

        for (IComicInfo comic : comics) {
            ContentValues comicValues = new ContentValues();

            comicValues.put(ComicEntry.COLUMN_COMIC_NUM, comic.getNum());
            comicValues.put(ComicEntry.COLUMN_COMIC_TITLE, comic.getTitle());
            comicValues.put(ComicEntry.COLUMN_COMIC_DATE, comic.getPostDate());
            comicValues.put(ComicEntry.COLUMN_COMIC_IMG, comic.getImageUrl());
            comicValues.put(ComicEntry.COLUMN_COMIC_ALT_TEXT, comic.getAltText());
            comicValues.put(ComicEntry.COLUMN_COMIC_LINK, comic.getUrl());
            comicValues.put(ComicEntry.COLUMN_COMIC_AUTHOR, comic.getAuthor());
            comicValues.put(ComicEntry.COLUMN_COMIC_NAME, mContext.getString(R.string.xkcd));

            cVVector.add(comicValues);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(ComicEntry.CONTENT_URI, cvArray);
        }
    }
}