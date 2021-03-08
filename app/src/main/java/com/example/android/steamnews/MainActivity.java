package com.example.android.steamnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rvArticleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.steam_icon);

        Toolbar toolbarBottom = findViewById(R.id.bottom_toolbar);
       toolbarBottom.inflateMenu(R.menu.toolbar_menu_items);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_icon:
                Log.d(TAG, "Setting Activity to Home");
                return true;
            case R.id.trending_icon:
                Log.d(TAG, "Setting Activity to Trending");
                return true;

            case R.id.options_icon:
                Log.d(TAG, "Setting Activity to Options");

            case R.id.search_icon:
                Log.d(TAG, "Setting Activity to Search");
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}