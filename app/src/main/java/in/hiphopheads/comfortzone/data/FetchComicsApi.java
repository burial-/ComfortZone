package in.hiphopheads.comfortzone.data;

import java.util.List;

import in.hiphopheads.comfortzone.data.comic.interfaces.amazingsuperpowersComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.buttersafeComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.pbfComicInfo;
import in.hiphopheads.comfortzone.data.comic.interfaces.xkcdComicInfo;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created on 18/08/14.
 */
public class FetchComicsApi {

    // A basic class that uses retrofit to get a list of comic items from our api
    //private static final String API_URL = "http://104.131.242.120";
    // ONE DAY WE WILL HAVE IT
    private static final String API_URL = "http://hiphopheads.in";

    public interface comicApi {
        @GET("/xkcd")
        List<xkcdComicInfo> getXkcdComics();

        @GET("/bs")
        List<buttersafeComicInfo> getButtersafeComics();

        @GET("/asp")
        List<amazingsuperpowersComicInfo> getAmazingsuperpowersComics();

        @GET("/pbf")
        List<pbfComicInfo> getPbfComics();
    }

    public List<xkcdComicInfo> fetchXkcdComics() throws Exception {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL) // The base API endpoint.
                .build()
                .create(comicApi.class)
                .getXkcdComics();
    }

    public List<buttersafeComicInfo> fetchButtersafeComics() throws Exception {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL) // The base API endpoint.
                .build()
                .create(comicApi.class)
                .getButtersafeComics();
    }

    public List<amazingsuperpowersComicInfo> fetchAmazingsuperpowersComics() throws Exception {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL) // The base API endpoint.
                .build()
                .create(comicApi.class)
                .getAmazingsuperpowersComics();
    }

    public List<pbfComicInfo> fetchPbfComics() throws Exception {
        return new RestAdapter.Builder()
                .setEndpoint(API_URL) // The base API endpoint.
                .build()
                .create(comicApi.class)
                .getPbfComics();
    }

}