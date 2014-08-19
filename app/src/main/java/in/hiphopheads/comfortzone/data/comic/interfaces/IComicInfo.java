package in.hiphopheads.comfortzone.data.comic.interfaces;

/**
 * Created on 18/08/14.
 */
public interface IComicInfo {

    // Gets the comics current ID
    public int getNum();

    // Gets the comics current post date
    public String getPostDate();

    // Gets the comics title
    public String getTitle();

    // Gets the comics image link
    public String getImageUrl();

    // Gets the comics alternative text/hover text
    public String getAltText();

    // Gets the comics hidden image link
    public String getHiddenImageUrl();

    // Gets the public link to the comic
    public String getUrl();

    // Gets the author of the current comic
    public String getAuthor();
}
