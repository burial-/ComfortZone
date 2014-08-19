package in.hiphopheads.comfortzone;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created on 18/08/14.
 */
public class ComicListAdapter extends CursorAdapter {

    // This is here in case we decide we want to change the list view,
    // or load certain items different
    int VIEW_TYPE_DEFAULT = 0;

    public ComicListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // If we create a new list item we need to know which view type its using
    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    // The amount of view types we have, increment this if you add a new one
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    // This method is used to bind the view with the cursor (set the data from the cursor
    // into the text views)
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Get the view holder
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Get the view type, commented out for now as we only have one type
        // int viewType = getItemViewType(cursor.getPosition());

        // Read title from cursor
        String title = cursor.getString(ComicsFragment.COL_COMIC_TITLE);
        // Find title TextView and set the title on it
        viewHolder.titleView.setText(title);

        // Read author from cursor
        String author = cursor.getString(ComicsFragment.COL_COMIC_AUTHOR);
        // Find the author TextView and set the author on it
        viewHolder.authorView.setText(author);

        // Read the comic name from the cursor
        String name = cursor.getString(ComicsFragment.COL_COMIC_NAME);
        // Find the comic name TextView and set the name on it
        viewHolder.comicNameView.setText(name);
    }

    // Just the basic method for creating a view
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Get layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        // we check our viewType and if it's found we choose a layout for our item
        if (viewType == VIEW_TYPE_DEFAULT){
            layoutId = R.layout.list_comic_item;
        }
        // Inflate the view, and set the view holder on it
        View view =  LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Cache of the children views for a comic list item.
     */
    public static class ViewHolder {
        // TODO: fix the button
        //public final ImageButton quickImageView;
        public final TextView titleView;
        public final TextView authorView;
        public final TextView comicNameView;

        public ViewHolder(View view) {
            //quickImageView = (ImageButton) view.findViewById(R.id.comic_list_item_quick);
            titleView = (TextView) view.findViewById(R.id.comic_list_item_title);
            authorView = (TextView) view.findViewById(R.id.comic_list_item_author);
            comicNameView = (TextView) view.findViewById(R.id.comic_list_item_name);
        }
    }

}