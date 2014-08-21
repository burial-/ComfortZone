package in.hiphopheads.comfortzone.data;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import in.hiphopheads.comfortzone.R;
import in.hiphopheads.comfortzone.data.comic.interfaces.amazingsuperpowersComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.buttersafeComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.pbfComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.xkcdComicInfo;
import in.hiphopheads.comfortzone.data.inserts.amazingsuperpowersInserts;
import in.hiphopheads.comfortzone.data.inserts.buttersafeInserts;
import in.hiphopheads.comfortzone.data.inserts.pbfInserts;
import in.hiphopheads.comfortzone.data.inserts.xkcdInserts;

/**
 * Created on 18/08/14.
 */
public class FetchComics extends AsyncTask<String, Void, Void> {

    // A basic Asynchronous task to download comics from the FetchComicsApi and then insert them
    // into the database with the corresponding comic insert class

    Context mContext;

    public FetchComics(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String... params) {
        // We get the comic name, this lets us decide which comic we will fetch comics for
        String comic_name = params[0];
        // May we wise to change to switch statement latter on in development,
        // but currently an if statement works well
        try {
            if (comic_name.equals(mContext.getString(R.string.xkcd))) {
                List<xkcdComicInfo> xkcd = new FetchComicsApi().fetchXkcdComics();
                new xkcdInserts(mContext).bulkInsertXkcdDatabase(xkcd);
            }
            if (comic_name.equals(mContext.getString(R.string.buttersafe))) {
                List<buttersafeComicInfo> buttersafe = new FetchComicsApi().fetchButtersafeComics();
                new buttersafeInserts(mContext).bulkInsertButtersafeDatabase(buttersafe);
            }
            if (comic_name.equals(mContext.getString(R.string.amazingsuperpowers))) {
                List<amazingsuperpowersComicInfo> amazingsuperpowers = new FetchComicsApi().fetchAmazingsuperpowersComics();
                new amazingsuperpowersInserts(mContext).bulkInsertAmazingsuperpowersDatabase(amazingsuperpowers);
            }
            if (comic_name.equals(mContext.getString(R.string.perrybiblefellowship))) {
                List<pbfComicInfo> pbf = new FetchComicsApi().fetchPbfComics();
                new pbfInserts(mContext).bulkInsertPbfDatabase(pbf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}