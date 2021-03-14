package com.example.android.steamnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.steamnews.data.GameAppIdItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
implements GameTitleAdapter.OnSearchResultClickListener{
    private final static String TAG = MainActivity.class.getSimpleName();
    //rv for listing the titles of games
    private RecyclerView rvArticleView;
    private GameTitleAdapter titleAdapter;
    private GameAppIdViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up view model to looks for changes in the bookmarked games
        this.viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(GameAppIdViewModel.class);
       this.viewModel.getBookmarkedGames().observe(
               this,
               new Observer<List<GameAppIdItem>>() {
                   @Override
                   public void onChanged(List<GameAppIdItem> gameAppIdItems) {
                       viewModel.getBookmarkedGames();
                       titleAdapter.updateSearchResults(gameAppIdItems);
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
              if(item.getItemId() == R.id.search_icon){
                  Log.d(TAG, "Setting Activity to Search");
              }else if(item.getItemId() == R.id.home_icon){
                  Log.d(TAG, "Setting Activity to Home");
              }else if(item.getItemId() == R.id.trending_icon){
                  Log.d(TAG, "Setting Activity to Trending");
              }else if(item.getItemId() == R.id.options_icon){
                  Log.d(TAG, "Setting Activity to Options");
              }
              //else none of the id's match
                return false;
            }

        });
        this.rvArticleView=findViewById(R.id.rv_game_title);
        this.rvArticleView.setLayoutManager(new LinearLayoutManager(this));
        this.rvArticleView.setHasFixedSize(true);
        this.titleAdapter= new GameTitleAdapter(this);
        this.rvArticleView.setAdapter(this.titleAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_game_search:
                Intent intent = new Intent(this, GameSearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onTitleResultClicked(GameAppIdItem gameAppidItem) {
        Log.d(TAG, "Clicked on the game: " + gameAppidItem.appId);
        int temp = gameAppidItem.appId;
        Log.d(TAG, "here is the appid in the main activity: " + temp);
        Intent intent = new Intent(this, DetailedArticleActivity.class);
        intent.putExtra("EXTRA_APPID", temp);
        startActivity(intent);
    }
}
