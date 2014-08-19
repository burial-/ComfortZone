package in.hiphopheads.comfortzone.data.comic.interfaces;

/**
 * Created on 18/08/14.
 */
public class xkcdComicInfo implements IComicInfo {


    public xkcdComicInfo(int num, String post_date, String title, String image_url, String alt_text, String url) {
        this.num = num;
        this.post_date = post_date;
        this.title = title;
        this.image_url = image_url;
        this.alt_text = alt_text;
        this.url = url;
    }

    private int num;
    private String alt_text, title, post_date, image_url, url;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAltText() {
        return alt_text;
    }

    @Override
    public String getAuthor() {
        return "Randall Munroe";
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public String getPostDate() {
        return post_date;
    }

    @Override
    public String getImageUrl() {
        return image_url;
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
