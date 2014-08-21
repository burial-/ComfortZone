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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;

import in.hiphopheads.comfortzone.data.ComicContract.ComicEntry;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A fragment made for viewing singular comics, it gets passed an id and then gets the comic
 * with the corresponding id.
 */
public class ComicViewerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // The key that's used to pass the comic id through
    public static final String COMIC_ID_KEY = "comic_id";
    // We want various methods to access the comic id.
    public String comicId;
    // A unique identifier for this loader. Can be whatever you want.
    private static final int DETAIL_LOADER = 0;

    // These are the various TextViews we use and need to change
    private TextView mComicAltTextHeader;
    private TextView mComicAltText;
    private TextView mComicTitle;
    private TextView mComicLinkHeader;
    private TextView mComicLink;
    private TextView mComicDate;
    private TextView mComicHiddenHeader;
    private TextView mComicHidden;
    private TextView mComicViewImg;
    // This is the layout that we set to GONE at the beginning,
    // it is a transparent overlay that holds one PhotoView for displaying the comic image
    private LinearLayout mComicOverlay;
    private PhotoView mComicImageOverlay;
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
        View rootView = inflater.inflate(R.layout.fragment_comic_viewer_v2, container, false);
        mComicImageOverlay = (PhotoView) rootView.findViewById(R.id.fragment_comic_image_overlay_v2);
        mComicOverlay = (LinearLayout) rootView.findViewById(R.id.overlay_layout);
        setOverlayVisible(false);

        mComicImage = (PhotoView) rootView.findViewById(R.id.fragment_comic_image_v2);
        mComicTitle = (TextView) rootView.findViewById(R.id.fragment_comic_title_v2);
        mComicAltText = (TextView) rootView.findViewById(R.id.fragment_comic_alt_v2);
        mComicAltTextHeader = (TextView) rootView.findViewById(R.id.fragment_comic_alt_head_v2);
        mComicLink = (TextView) rootView.findViewById(R.id.fragment_comic_link_v2);
        mComicLinkHeader = (TextView) rootView.findViewById(R.id.fragment_comic_link_head_v2);
        mComicDate = (TextView) rootView.findViewById(R.id.fragment_comic_date_v2);
        mComicHiddenHeader = (TextView) rootView.findViewById(R.id.fragment_comic_hidden_head_v2);
        mComicHidden = (TextView) rootView.findViewById(R.id.fragment_comic_hidden_v2);
        mComicViewImg = (TextView) rootView.findViewById(R.id.fragment_comic_view_img_btn_v2);
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
        /*
         * In this method we take all the data and set all the text views to use it.
         * We also set onclick listeners for all the views we want to respond to user input
         */
        if (data != null && data.moveToFirst()) {
            // Get the image and then load it into the photoview
            final String imageUri = data.getString(data.getColumnIndex((ComicEntry.COLUMN_COMIC_IMG)));
            if (imageUri != null) {
                Picasso.with(getActivity()).load(imageUri).into(mComicImage);
                // Now set an on click listener to overlay the image
                mComicImage.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v2) {
                        Picasso.with(getActivity()).load(imageUri).into(mComicImageOverlay);
                        setOverlayVisible(true);
                        mComicImageOverlay.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                            @Override
                            public void onPhotoTap(View view, float v, float v2) {
                                setOverlayVisible(false);
                            }
                        });
                    }
                });
            }

            // Get the title and set the text for it, afterwards set onclick listeners for the title
            // and the view image text to display the comic image
            String title = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_TITLE));
            mComicTitle.setText(title);
            if (imageUri != null) {
                mComicTitle.setOnClickListener(onClickDisplayOverlay(imageUri));
                mComicViewImg.setOnClickListener(onClickDisplayOverlay(imageUri));
            }

            // Get the date then convert it using the formatDate class in Utility to make it
            // user friendly, we have a parse error then we just display the date in its naked form
            String date = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_DATE));
            try {
                mComicDate.setText(new Utility(getActivity()).formatDate(date));
            } catch (ParseException e) {
                mComicDate.setText(date);
            }

            // Get the alternative text and set it if it exists, otherwise remove the text views
            String altText = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_ALT_TEXT));
            if (altText == null) {
                mComicAltText.setVisibility(View.GONE);
                mComicAltTextHeader.setVisibility(View.GONE);
            } else {
                mComicAltText.setText(altText);
            }

            // Get the comic url and set it if it exists, also include an onclicklistener that will
            // use a action_view intent which means it will launch the users browser with the url
            final String link = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_LINK));
            if (link == null) {
                mComicLink.setVisibility(View.GONE);
                mComicLinkHeader.setVisibility(View.GONE);
            } else {
                mComicLink.setText(link);
                mComicLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(link)));
                    }
                });
            }

            // Grab the hidden comic string, if one doesn't exist then set the view to gone, otherwise
            // set the text then set an on click listener to load the hidden comic image
            final String hiddenImg = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_HIDDEN_IMG));
            if (hiddenImg == null) {
                mComicHidden.setVisibility(View.GONE);
                mComicHiddenHeader.setVisibility(View.GONE);
            } else {
                mComicHidden.setText(hiddenImg);
                Picasso.with(getActivity()).load(hiddenImg);
                mComicHidden.setOnClickListener(onClickDisplayOverlay(hiddenImg));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }

    // A basic function to turn the overlay on/off
    public void setOverlayVisible(Boolean visible) {
        if (visible) {
            mComicOverlay.setVisibility(View.VISIBLE);
            mComicImageOverlay.setVisibility(View.VISIBLE);
        } else {
            mComicImageOverlay.setVisibility(View.GONE);
            mComicOverlay.setVisibility(View.GONE);
        }
    }

    // An OnClickListener for the overlay, it sets the PhotoView to have an
    // OnPhotoTapListener as well which is used to exit the overlay
    public View.OnClickListener onClickDisplayOverlay(final String imageUri) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getActivity()).load(imageUri).into(mComicImageOverlay);
                setOverlayVisible(true);
                mComicImageOverlay.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v2) {
                        setOverlayVisible(false);
                    }
                });
            }
        };
        return onClickListener;
    }

}
