package in.hiphopheads.comfortzone;

import android.os.Bundle;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ComicViewer extends SwipeBackActivity {

    // A basic class for the ComicViewerFragment, it extends SwipeBackActivity
    // to allow the user to swipe there screen from the left to go back to the
    // previous screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_viewer);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ComicViewerFragment())
                    .commit();
        }
    }

}
