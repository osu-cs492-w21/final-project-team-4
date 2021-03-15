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
import com.example.android.steamnews.data.GameAppIdItem;
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
        setContentView(R.layout.activity_main);

        this.viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(TrendingViewModel.class);

        this.viewModel.getBookmarkedGames().observe(
                this,
                new Observer<List<TrendingDataItem>>() {
                    @Override
                    public void onChanged(List<TrendingDataItem> gameAppIdItems) {
                        viewModel.fetchTrendingList();
                        articleAdapter.updateSearchResults(gameAppIdItems);
                    }
                }
        );

        //toolbar object for the upper toolbar (where the title is set)
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

        this.articleDetailRV=findViewById(R.id.rv_game_title);
        this.articleDetailRV.setLayoutManager(new LinearLayoutManager(this));
        this.articleDetailRV.setHasFixedSize(true);
        this.articleAdapter= new TrendingAdapter(this);
        this.articleDetailRV.setAdapter(this.articleAdapter);
    }




    @Override
    public void onTitleResultClicked(TrendingDataItem gameAppidItem) {
        Log.d(TAG, "Clicked on the game: " + gameAppidItem.appID);
        int temp = gameAppidItem.appID;
        Log.d(TAG, "here is the appid in the main activity: " + temp);
        Intent intent = new Intent(this, DetailedArticleActivity.class);
        intent.putExtra("EXTRA_APPID", temp);
        startActivity(intent);
    }


}
