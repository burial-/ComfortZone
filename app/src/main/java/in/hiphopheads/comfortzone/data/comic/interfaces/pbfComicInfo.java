package in.hiphopheads.comfortzone.data.comic.interfaces;

/**
 * Created on 19/08/14.
 */
public class pbfComicInfo implements IComicInfo {

    public pbfComicInfo(Integer num, String img_url, String title, String url) {
        this.num = num;
        this.title = title;
        this.img_url = img_url;
        this.url = url;
    }
    int num;
    private String title, img_url, url;

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
        return "Nicholas Gurewitch";
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public String getPostDate() {
        return "2010-01-01";
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
