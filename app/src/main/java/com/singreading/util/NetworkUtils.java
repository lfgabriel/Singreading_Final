package com.singreading.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.singreading.model.Lyric;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by gabriel on 08/04/18.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_LYRICAPI_URL =
            "http://lyric-api.herokuapp.com/api/find";


    /* The format we want our API to return */
    private static final String format = "json";

    /* API key. You must set it here */
    final static String APIKeyValue = "";

    final static String SIZE_PARAM = "w";
    final static String API_KEY = "api_key";


    /**
     * Builds the URL used to fetch lyric.
     *
     * @return The URL to use to query the server.
     */
    public static URL buildUrlForSearchingLyric(String artistName, String musicName) {

        Uri builtUri = Uri.parse(BASE_LYRICAPI_URL).buildUpon()
                .appendPath(artistName)
                .appendPath(musicName)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }


    public static String getLyricFromJson(Context context, String lyricJsonStr) throws JSONException {

        final String LYRIC = "lyric";
        final String ERROR = "err";

        String lyric = "";
        String error = "";

        JSONObject lyricJson = new JSONObject(lyricJsonStr);
        error = lyricJson.getString(ERROR);
        if (error.equals("none")) {
            lyric = lyricJson.getString(LYRIC);
            return lyric;
        }
        else
            return error;
    }
}
