package in.hiphopheads.comfortzone;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ListView;

import in.hiphopheads.comfortzone.data.ComicContract;
import in.hiphopheads.comfortzone.data.FetchComics;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String COMIC_SEARCH_TERM = "search_term";


    private static String[] COMIC_COLUMNS = new String[]{
            ComicContract.ComicEntry.TABLE_NAME + "." + ComicContract.ComicEntry._ID,
            ComicContract.ComicEntry.COLUMN_COMIC_TITLE,
            ComicContract.ComicEntry.COLUMN_COMIC_AUTHOR,
            ComicContract.ComicEntry.COLUMN_COMIC_NAME
    };

    // These indices are tied to COMIC_COLOUMNS.  If COMIC_COLOUMNS changes, these
    // must change.
    public static final int COL_COMIC_ID = 0;
    public static final int COL_COMIC_TITLE = 1;
    public static final int COL_COMIC_AUTHOR = 2;
    public static final int COL_COMIC_NAME = 3;

    // This is the adapter we use to load all the list items
    private ComicListAdapter comicListAdapter;
    // A unique identifier for this loader. Can be whatever you want.
    private static final int COMIC_LOADER = 0;

    public int COMIC_NUMBER;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ComicsFragment newInstance(int sectionNumber, Boolean searching, String searchTerm) {
        ComicsFragment fragment = new ComicsFragment();
        Bundle args = new Bundle();
        if (searching) {
            args.putString(COMIC_SEARCH_TERM, searchTerm);
        }
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ComicsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        comicListAdapter = new ComicListAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_comics, container, false);
        // Get a reference to the list view and attach this adapter to it
        ListView mComicList = (ListView) rootView.findViewById(
                R.id.listview_comics);
        mComicList.setAdapter(comicListAdapter);

        mComicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = comicListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Intent intent = new Intent(getActivity(), ComicViewer.class)
                            .putExtra(ComicViewerFragment.COMIC_ID_KEY, cursor.getString(COL_COMIC_ID));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Comics) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        COMIC_NUMBER = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (COMIC_NUMBER) {
            case 1:
                new FetchComics(getActivity()).execute(getString(R.string.xkcd));
                new FetchComics(getActivity()).execute(getString(R.string.buttersafe));
                new FetchComics(getActivity()).execute(getString(R.string.amazingsuperpowers));
                new FetchComics(getActivity()).execute(getString(R.string.perrybiblefellowship));
                break;
            case 2:
                new FetchComics(getActivity()).execute(getString(R.string.xkcd));
                break;
            case 3:
                new FetchComics(getActivity()).execute(getString(R.string.buttersafe));
                break;
            case 4:
                new FetchComics(getActivity()).execute(getString(R.string.amazingsuperpowers));
                break;
            case 5:
                new FetchComics(getActivity()).execute(getString(R.string.perrybiblefellowship));
            default:
                new FetchComics(getActivity()).execute(getString(R.string.xkcd));
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(COMIC_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.
        // These are used to determine the provider Uri we pass to the cursor loader
        Uri comicUri = null;
        String sortOrder = null;

        // Sort order:  Ascending, by date.
        sortOrder = ComicContract.ComicEntry.COLUMN_COMIC_DATE + " DESC";
        comicUri = ComicContract.ComicEntry.CONTENT_URI;
        String queryArgs = null;
        switch (COMIC_NUMBER) {
            case 2:
                queryArgs = ComicContract.ComicEntry.COLUMN_COMIC_NAME + " = '" + getActivity().getString(R.string.xkcd) + "'";
                break;
            case 3:
                queryArgs = ComicContract.ComicEntry.COLUMN_COMIC_NAME + " = '" + getActivity().getString(R.string.buttersafe) + "'";
                break;
            case 4:
                queryArgs = ComicContract.ComicEntry.COLUMN_COMIC_NAME + " = '" + getActivity().getString(R.string.amazingsuperpowers) + "'";
                break;
            case 5:
                queryArgs = ComicContract.ComicEntry.COLUMN_COMIC_NAME + " = '" + getActivity().getString(R.string.perrybiblefellowship) + "'";
                break;
        }

        if (getArguments().containsKey(COMIC_SEARCH_TERM)) {
            if (queryArgs != null) {
                queryArgs = queryArgs + " AND " + ComicContract.ComicEntry.COLUMN_COMIC_TITLE + " LIKE '%" + getArguments().getString(COMIC_SEARCH_TERM) + "%'";
            } else {
                queryArgs = ComicContract.ComicEntry.COLUMN_COMIC_TITLE + " LIKE '%" + getArguments().getString(COMIC_SEARCH_TERM) + "%'";
            }
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                comicUri,
                COMIC_COLUMNS,
                //ComicContract.ComicEntry.COLUMN_COMIC_NAME + " = '" + getActivity().getString(R.string.perrybiblefellowship) + "'",
                queryArgs,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        comicListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        comicListAdapter.swapCursor(null);
    }
}