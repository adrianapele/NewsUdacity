package com.example.adriana.news;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Adriana on 7/21/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();

        Toast.makeText(getContext(), R.string.message_to_wait, Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<News> newsList = QueryUtils.fetchNewsData(mUrl);
        return newsList;
    }
}