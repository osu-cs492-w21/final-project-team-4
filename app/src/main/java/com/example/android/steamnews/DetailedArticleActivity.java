package com.example.android.steamnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.ArticleData;
import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.GameAppIdItem;

import java.util.List;

public class DetailedArticleActivity extends AppCompatActivity implements  ArticleAdapter.OnSearchResultClickListener{
    private static final String TAG = DetailedArticleActivity.class.getSimpleName();
    private RecyclerView articleDetailRV;
    private ArticleAdapter articleAdapter;
    private ArticleViewModel viewModel;
    private final DetailedArticleActivity lifecycleowner = this;


private final DetailedArticleActivity lifecycleOwner = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_layout);
        this.articleDetailRV = findViewById(R.id.rv_articles);
        this.articleDetailRV.setLayoutManager(new LinearLayoutManager(this));
        this.articleDetailRV.setHasFixedSize(true);

        this.articleAdapter = new ArticleAdapter(this);
        this.articleDetailRV.setAdapter(articleAdapter);
        this.viewModel = new ViewModelProvider(this)
                .get(ArticleViewModel.class);
        int tempAppId = getIntent().getIntExtra("EXTRA_APPID", 0);
        Log.d(TAG, "Here is the appid sent into the activity: " + tempAppId);
        viewModel.loadArticles(tempAppId);

        this.viewModel.getArticleData().observe(
                this,
                new Observer<List<ArticleDataItem>>() {
                    @Override
                    public void onChanged(List<ArticleDataItem> articleDataItems) {
                        articleAdapter.updateSearchResults(articleDataItems);
                    }
                }
        );


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the icon next to the title to the Steam Icon
        getSupportActionBar().setIcon(R.drawable.steam_icon);
        //create a toolbar object for the bottom toolbar
        Toolbar toolbarBottom = findViewById(R.id.bottom_toolbar);
        toolbarBottom.inflateMenu(R.menu.toolbar_menu_items);
        //Listeners for clicks on the buttons on the bottom of the screen.
        //These can be used to switch between activities
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_icon) {
                    Log.d(TAG, "Setting Activity to Search");
                    Intent intent = new Intent(DetailedArticleActivity.this, GameSearchActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.home_icon) {
                    Log.d(TAG, "Setting Activity to Home");
                    openHomePage();
                } else if (item.getItemId() == R.id.trending_icon) {
                    Log.d(TAG, "Setting Activity to Trending");
                    Intent profileIntent = new Intent(DetailedArticleActivity.this, TrendingActivity.class);
                    startActivity(profileIntent);
                } else if (item.getItemId() == R.id.account_icon) {
                    Log.d(TAG, "Setting Activity to Options");
                    openProfilePage();
                }
                //else none of the id's match
                return false;

            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(DetailedArticleActivity.this, Settings.class);
                startActivity(profileIntent);
            }

            public void openHomePage() {
                Intent profileIntent = new Intent(DetailedArticleActivity.this, MainActivity.class);
                startActivity(profileIntent);
            }


        });


    }




    @Override
    public void onSearchResultClicked(ArticleDataItem articleDataItem) {
        Log.d(TAG, "Opening the article in a browser: " + articleDataItem.title);
        Log.d(TAG, "Here is the URL: " + articleDataItem.url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleDataItem.url));
        startActivity(intent);
    }


}

