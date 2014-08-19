package in.hiphopheads.comfortzone.data.comic.interfaces;

/**
 * Created on 19/08/14.
 */
public class amazingsuperpowersComicInfo implements IComicInfo {

    public amazingsuperpowersComicInfo(String post_date, String title, String image_url, String alt_text, String hidden_comic_img_url, String url) {
        this.post_date = post_date;
        this.title = title;
        this.image_url = image_url;
        this.alt_text = alt_text;
        this.hidden_comic_img_url = hidden_comic_img_url;
        this.url = url;
    }

    private String title, post_date, image_url, alt_text, hidden_comic_img_url, url;

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
        return "Wes & Tony";
    }

    @Override
    public int getNum() {
        return 0;
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
        return hidden_comic_img_url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
