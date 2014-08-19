package in.hiphopheads.comfortzone;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.hiphopheads.comfortzone.data.ComicContract.ComicEntry;
import uk.co.senab.photoview.PhotoView;

/**
 * A fragment made for viewing singular comics, it gets passed an id and then gets the comic
 * with the corresponding id.
 */
public class ComicViewerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // The key that's used to pass the comic id through
    public static final String COMIC_ID_KEY = "comic_id";
    // We want various methods to access the comic id.
    public String comicId;
    // A unique identifier for this loader. Can be whatever you want.
    private static final int DETAIL_LOADER = 0;
    // This will only be filled if a comic has alt text
    private TextView mComicAltText;
    // The comics image, which is displayed using the PhotoView library
    private PhotoView mComicImage;

    // This is the columns we will load for the particular comic,
    // as this time we load them all as we want to access everything
    public String COMIC_COLUMNS[] = new String[]{
            ComicEntry.TABLE_NAME + "." + ComicEntry._ID,
            ComicEntry.COLUMN_COMIC_NUM,
            ComicEntry.COLUMN_COMIC_DATE,
            ComicEntry.COLUMN_COMIC_TITLE,
            ComicEntry.COLUMN_COMIC_AUTHOR,
            ComicEntry.COLUMN_COMIC_IMG,
            ComicEntry.COLUMN_COMIC_NAME,
            ComicEntry.COLUMN_COMIC_ALT_TEXT,
            ComicEntry.COLUMN_COMIC_LINK,
            ComicEntry.COLUMN_COMIC_HIDDEN_IMG
    };

    // These indices are tied to COMIC_COLUMNS. If COMIC_COLUMNS changes, these
    // must change.
    public static final int COL_COMIC_ID = 0;
    public static final int COL_COMIC_NUM = 1;
    public static final int COL_COMIC_DATE = 2;
    public static final int COL_COMIC_TITLE = 3;
    public static final int COL_COMIC_AUTHOR = 4;
    public static final int COL_COMIC_IMG = 5;
    public static final int COL_COMIC_NAME = 6;
    public static final int COL_COMIC_ALT_TEXT = 7;
    public static final int COL_COMIC_LINK = 8;
    public static final int COL_COMIC_HIDDEN_IMG = 9;


    /**
     * Returns a new instance of this fragment for the given comic id.
     */
    public static ComicViewerFragment newInstance(String comicId) {
        ComicViewerFragment fragment = new ComicViewerFragment();
        Bundle args = new Bundle();
        args.putString(COMIC_ID_KEY, comicId);
        fragment.setArguments(args);
        return fragment;
    }

    public ComicViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // First we get the intent for the fragment
        Intent intent = getActivity().getIntent();
        // We then get the comic id
        comicId = intent.getStringExtra(COMIC_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_viewer, container, false);
        mComicImage = (PhotoView) rootView.findViewById(R.id.comic_image);
        mComicAltText = (TextView) rootView.findViewById(R.id.comic_alt_text);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.
        // If we have no comic id we can't load anything so we just quit
        if (comicId == null) {
            return null;
        }

        // These are used to determine the provider Uri we pass to the cursor loader
        Uri comicUri = ComicEntry.buildComicUri(Long.parseLong(comicId));

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                comicUri,
                COMIC_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        /** We grab the title string, we then set the actionbar text to the title,
         * if that fails we just use a default title, we then get the alternative text
         * and display it, if it's null it just won't be displayed. Finally,
         * we load the image using Picasso
         */
        if (data != null && data.moveToFirst()) {
            String title = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_TITLE));
            try {
                getActivity().getActionBar().setTitle(title);
            } catch (Exception e) {
                getActivity().getActionBar().setTitle("Comic Display");
            }
            String altText = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_ALT_TEXT));
            mComicAltText.setText(altText);
            String imageUri = data.getString(data.getColumnIndex((ComicEntry.COLUMN_COMIC_IMG)));
            Picasso.with(getActivity()).load(imageUri).into(mComicImage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }
}
