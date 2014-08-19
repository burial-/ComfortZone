package in.hiphopheads.comfortzone.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created on 18/08/14.
 */
public class ComicContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "in.hiphopheads.comfortzone";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://in.hiphopheads.scumcom.data.xkcd/ is a valid path for
    // looking at weather data. content://in.hiphopheads.scumcom/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_COMIC = "comics";

    public static final class ComicEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMIC).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_COMIC;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_COMIC;

        // Table name
        public static final String TABLE_NAME = "comics";

        public static final String COLUMN_COMIC_NUM = "comic_num";

        public static final String COLUMN_COMIC_DATE = "comic_date";

        public static final String COLUMN_COMIC_TITLE = "comic_title";

        public static final String COLUMN_COMIC_IMG = "comic_img";

        public static final String COLUMN_COMIC_ALT_TEXT = "comic_alt_text";

        public static final String COLUMN_COMIC_LINK = "comic_link";

        public static final String COLUMN_COMIC_HIDDEN_IMG = "comic_hidden";

        public static final String COLUMN_COMIC_AUTHOR = "comic_author";

        public static final String COLUMN_COMIC_NAME = "comic_name";

        public static Uri buildComicUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
