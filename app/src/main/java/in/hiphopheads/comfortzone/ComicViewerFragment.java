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
import java.util.Calendar;
import java.util.Date;

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
    // This will only be filled if a comic has alt text
    private TextView mComicAltTextHeader;
    private TextView mComicAltText;
    private TextView mComicTitle;
    private TextView mComicLinkHeader;
    private TextView mComicLink;
    private TextView mComicDate;
    private TextView mComicHiddenHeader;
    private TextView mComicHidden;
    private TextView mComicViewImg;
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
        //View rootView = inflater.inflate(R.layout.fragment_comic_viewer, container, false);
        View rootView = inflater.inflate(R.layout.fragment_comic_viewer_v2, container, false);
        mComicImageOverlay = (PhotoView) rootView.findViewById(R.id.fragment_comic_image_overlay_v2);
        mComicOverlay = (LinearLayout) rootView.findViewById(R.id.overlay_layout);
        mComicImageOverlay.setVisibility(View.GONE);
        mComicOverlay.setVisibility(View.GONE);
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
        /** We grab the title string, we then set the actionbar text to the title,
         * if that fails we just use a default title, we then get the alternative text
         * and display it, if it's null it just won't be displayed. Finally,
         * we load the image using Picasso
         */
        if (data != null && data.moveToFirst()) {
            String title = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_TITLE));
            mComicTitle.setText(title);
            String date = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_DATE));
            try {
                mComicDate.setText(formatDate(date));
            } catch (ParseException e) {
                mComicDate.setText(date);
            }
            String altText = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_ALT_TEXT));
            if (altText == null) {
                mComicAltText.setVisibility(View.GONE);
                mComicAltTextHeader.setVisibility(View.GONE);
            } else {
                mComicAltText.setText(altText);
            }
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

            final String imageUri = data.getString(data.getColumnIndex((ComicEntry.COLUMN_COMIC_IMG)));
            if (imageUri != null) {
                Picasso.with(getActivity()).load(imageUri).into(mComicImage);
            }

            mComicTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Picasso.with(getActivity()).load(imageUri).into(mComicImageOverlay);
                    mComicOverlay.setVisibility(View.VISIBLE);
                    mComicImageOverlay.setVisibility(View.VISIBLE);
                    mComicImageOverlay.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View view, float v, float v2) {
                            mComicImageOverlay.setVisibility(View.GONE);
                            mComicOverlay.setVisibility(View.GONE);
                        }
                    });
                }
            });

            mComicViewImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Picasso.with(getActivity()).load(imageUri).into(mComicImageOverlay);
                    mComicOverlay.setVisibility(View.VISIBLE);
                    mComicImageOverlay.setVisibility(View.VISIBLE);
                    mComicImageOverlay.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View view, float v, float v2) {
                            mComicImageOverlay.setVisibility(View.GONE);
                            mComicOverlay.setVisibility(View.GONE);
                        }
                    });
                }
            });

            final String hiddenImg = data.getString(data.getColumnIndex(ComicEntry.COLUMN_COMIC_HIDDEN_IMG));
            if (hiddenImg == null) {
                mComicHidden.setVisibility(View.GONE);
                mComicHiddenHeader.setVisibility(View.GONE);
            } else {
                mComicHidden.setText(hiddenImg);
                Picasso.with(getActivity()).load(hiddenImg);
                mComicHidden.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getActivity()).load(hiddenImg).into(mComicImageOverlay);
                        mComicOverlay.setVisibility(View.VISIBLE);
                        mComicImageOverlay.setVisibility(View.VISIBLE);
                        mComicImageOverlay.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                            @Override
                            public void onPhotoTap(View view, float v, float v2) {
                                mComicImageOverlay.setVisibility(View.GONE);
                                mComicOverlay.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }

    // This is a plethora of code that just converts the date to a readable format
    // (2014-01-18 = Tuesday, the 1st of January, 2014)
    public String formatDate(String date) throws ParseException {
        java.text.SimpleDateFormat simpleDateFormatForParse = new java.text.SimpleDateFormat("yyyy-mm-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormatForParse.parse(date));
        Date currentDate = cal.getTime();
        java.text.SimpleDateFormat simpleDateFormatForDay = new java.text.SimpleDateFormat("d");
        java.text.SimpleDateFormat simpleDateFormatForDayName = new java.text.SimpleDateFormat("EEEE");
        String dayName = simpleDateFormatForDayName.format(currentDate);
        Integer day = Integer.parseInt(simpleDateFormatForDay.format(currentDate));
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MMMM, yyyy");
        return dayName +", the " +day + getDayOfMonthSuffix(day) + " of "  + simpleDateFormat.format(currentDate);
    }

    // Because someone overlooked the idea that you would want a day of month suffix in the
    // SimpleDateFormat utility we have this here.
    public String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

}
