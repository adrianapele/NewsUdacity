package com.example.adriana.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SECTION_NAME = "sectionName";
    public static final String WEB_URL = "webUrl";
    public static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    public static final String PUBLICATION_DATE = "webPublicationDate";
    public static final String WEB_TITLE = "webTitle";
    public static final String RESPONSE = "response";
    public static final String RESULTS = "results";
    public static final String SHOW_TAGS = "show-tags";
    public static final int LOADER_ID = 1;
    public static final String API_KEY = "api-key";
    public static final String FROM_DATE = "from-date";
    public static final String TOPIC = "q";

    private NewsAdapter newsAdapter;
    private TextView emptyTextView;
    
    private String guardian_api = "992884c0-72db-44e8-9962-ef1ae9781f13";
    private String contributor = "contributor";
    private String baseURL = "https://content.guardianapis.com/search?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.list);

        emptyTextView = ( TextView ) findViewById(R.id.empty);
        emptyTextView.setText("");
        listView.setEmptyView(emptyTextView);

        newsAdapter = new NewsAdapter(getApplicationContext(), new ArrayList<News>());
        listView.setAdapter(newsAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newsAdapter.getItem(position).getWebURL();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        if (verifyInternetConnection()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            emptyTextView.setText(R.string.no_internet_message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean verifyInternetConnection() {
        ConnectivityManager connMgr = ( ConnectivityManager ) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        String from_date = sharedPreferences.getString(getString(R.string.settings_from_date_key), getString(R.string.settings_from_date_default));

        String topic = sharedPreferences.getString(getString(R.string.settings_topic_key), getString(R.string.settings_topic_default));

        Uri baseUri = Uri.parse(baseURL);

        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter(TOPIC, topic);
        builder.appendQueryParameter(FROM_DATE, from_date);
        builder.appendQueryParameter(SHOW_TAGS, contributor);
        builder.appendQueryParameter(API_KEY, guardian_api);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> data) {
        emptyTextView.setText(R.string.no_news_found);

        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.settings_from_date_key)) ||
               key.equals(getString(R.string.settings_topic_key)) ) {
            
            newsAdapter.clear();

            emptyTextView.setVisibility(View.GONE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }
}
