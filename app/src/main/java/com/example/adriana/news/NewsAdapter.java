package com.example.adriana.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adriana on 7/21/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public static final String AUTHOR = "Author: ";
    public static final String DATE = "Date: ";
    public static final String TOPIC = "Topic: ";
    public static final String URL = "URL: ";
    private Context context;

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View newsView = convertView;

        if (newsView == null) {
            newsView = LayoutInflater.from(context).inflate(R.layout.news_list, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = newsView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getTitle());

        TextView authorTextView = newsView.findViewById(R.id.author_text_view);
        authorTextView.setText(AUTHOR + currentNews.getAuthor());

        TextView dateTextView = newsView.findViewById(R.id.date_text_view);
        dateTextView.setText(DATE + currentNews.getDate());

        TextView topicTextView = newsView.findViewById(R.id.topic_text_view);
        topicTextView.setText(TOPIC + currentNews.getTopic());

        TextView urlTextView = newsView.findViewById(R.id.URL_text_view);
        urlTextView.setText(URL + currentNews.getWebURL());

        return newsView;
    }

}
