package in.hiphopheads.comfortzone.data.comic.interfaces;

/**
 * Created on 18/08/14.
 */
public class buttersafeComicInfo implements IComicInfo {


    public buttersafeComicInfo(String date_posted, String title, String img_url, String url) {
        this.date_posted = date_posted;
        this.title = title;
        this.img_url = img_url;
        this.url = url;
    }

    private String title, date_posted, img_url, url;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAltText() {
        return null;
    }

    @Override
    public String getAuthor() {
        return "Raynato Castro, Alex Culang";
    }

    @Override
    public int getNum() {
        return 0;
    }

    @Override
    public String getPostDate() {
        return date_posted;
    }

    @Override
    public String getImageUrl() {
        return img_url;
    }

    @Override
    public String getHiddenImageUrl() {
        return null;
    }

    @Override
    public String getUrl() {
        return url;
    }
}

