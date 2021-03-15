package com.example.android.steamnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.TrendingDataItem;

import java.util.List;

public class TrendingActivity extends AppCompatActivity
        implements TrendingAdapter.OnSearchResultClickListener {
    private final static String TAG = TrendingActivity.class.getSimpleName();
    private Toolbar menutoolbar;
    private Toolbar toptoolbar;
    private RecyclerView articleDetailRV;
    private TrendingAdapter articleAdapter;
    private TrendingViewModel viewModel;
    private final TrendingActivity lifecycleowner = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_layout);
        this.articleDetailRV = findViewById(R.id.rv_articles);
        this.articleDetailRV.setLayoutManager(new LinearLayoutManager(this));
        this.articleDetailRV.setHasFixedSize(true);

        this.articleAdapter = new TrendingAdapter(this);
        this.articleDetailRV.setAdapter(articleAdapter);
        this.viewModel = new ViewModelProvider(this)
                .get(TrendingViewModel.class);

        int tempAppId = getIntent().getIntExtra("EXTRA_APPID", 0);
        Log.d(TAG, "Here is the appid sent into the activity: " + tempAppId);
        viewModel.loadArticles(tempAppId);

        this.viewModel.getArticleData().observe(
                this,
                new Observer<List<TrendingDataItem>>() {
                    @Override
                    public void onChanged(List<TrendingDataItem> articleDataItems) {
                        articleAdapter.updateSearchResults(articleDataItems);
                    }
                }
        );

        menutoolbar = (Toolbar) findViewById(R.id.bottom_toolbar);
        menutoolbar.inflateMenu(R.menu.toolbar_menu_items);
        menutoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_icon) {
                    Log.d(TAG, "Setting Activity to Search");
                } else if (item.getItemId() == R.id.home_icon) {
                    Log.d(TAG, "Setting Activity to Home");
                    openHomePage();
                } else if (item.getItemId() == R.id.trending_icon) {
                    Log.d(TAG, "Setting Activity to Trending");
                } else if (item.getItemId() == R.id.account_icon) {
                    Log.d(TAG, "Setting Activity to Options");
                    openProfilePage();
                }
                //else none of the id's match
                return false;
            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(TrendingActivity.this, Settings.class);
                startActivity(profileIntent);
            }

            public void openHomePage() {
                Intent profileIntent = new Intent(TrendingActivity.this, MainActivity.class);
                startActivity(profileIntent);
            }
        });
    }




    @Override
    public void onSearchResultClicked(TrendingDataItem articleDataItem) {
        Log.d(TAG, "app id is: " + articleDataItem.appID);
        //Log.d(TAG, "Here is the URL: " + articleDataItem.url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleDataItem.appID));
        startActivity(intent);
    }

}
