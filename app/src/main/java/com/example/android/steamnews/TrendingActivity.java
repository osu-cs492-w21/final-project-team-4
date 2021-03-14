package com.example.android.steamnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.GameAppIdItem;

public class TrendingActivity extends AppCompatActivity
        implements GameSearchAdapter.OnSearchResultClickListener {
    private final static String TAG = TrendingActivity.class.getSimpleName();
    private Toolbar menutoolbar;
    private Toolbar toptoolbar;
    private GameAppIdViewModel viewModel;
    private GameSearchAdapter gameSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        getTrending();

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

    private void getTrending() {
        Log.d(TAG, "Getting Trending");

    }


    @Override
    public void onSearchResultClicked(GameAppIdItem gameAppidItem) {
        gameAppidItem.bookmarked = !gameAppidItem.bookmarked;

        this.viewModel.insertGameAppIdItem(gameAppidItem);

        this.gameSearchAdapter.notifyDataSetChanged();
    }

}
