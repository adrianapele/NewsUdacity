package com.example.adriana.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.adriana.news.MainActivity.PUBLICATION_DATE;
import static com.example.adriana.news.MainActivity.RESPONSE;
import static com.example.adriana.news.MainActivity.RESULTS;
import static com.example.adriana.news.MainActivity.SECTION_NAME;
import static com.example.adriana.news.MainActivity.WEB_PUBLICATION_DATE;
import static com.example.adriana.news.MainActivity.WEB_TITLE;
import static com.example.adriana.news.MainActivity.WEB_URL;

/**
 * Created by Adriana on 7/21/2018.
 */

public final class QueryUtils {

    public static final String TAGS = "tags";
    public static final String DEFAULT_VALUE_AUTHOR_NAME = "no author found";
    public static final String REQUEST_METHOD = "GET";
    public static final int SUCCESS_RESPONSE = 200;
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECT_TIMEOUT = 15000;

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<News> newsList = extractFeatureFromJson(jsonResponse);

        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = ( HttpURLConnection ) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == SUCCESS_RESPONSE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSONResponse) {
        if (TextUtils.isEmpty(newsJSONResponse)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(newsJSONResponse);
            JSONObject responseJSONObject = response.getJSONObject(RESPONSE);
            JSONArray results = responseJSONObject.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject firstResultJSONObject = results.getJSONObject(i);

                String title = firstResultJSONObject.getString(WEB_TITLE);

                String date = "";
                if (firstResultJSONObject.getString(PUBLICATION_DATE) != null)
                    date = firstResultJSONObject.getString(WEB_PUBLICATION_DATE);

                String webUrl = firstResultJSONObject.getString(WEB_URL);
                String topic = firstResultJSONObject.getString(SECTION_NAME);

                String author = DEFAULT_VALUE_AUTHOR_NAME;
                if (firstResultJSONObject.has(TAGS)) {
                    JSONArray tagsJSONArray = firstResultJSONObject.getJSONArray(TAGS);

                    if (tagsJSONArray != null) {
                        if (tagsJSONArray.length() > 0) {
                            JSONObject tagsJSONObject = tagsJSONArray.getJSONObject(0);

                            if (tagsJSONObject != null)
                                if (tagsJSONObject.has(WEB_TITLE))
                                    author = tagsJSONObject.getString(WEB_TITLE);
                        }
                    }
                }

                News news = new News(title, author, date, topic, webUrl);

                newsList.add(news);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
