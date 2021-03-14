package com.example.android.steamnews;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.ArticleData;
import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.GameAppIdItem;

import java.util.List;

public class DetailedArticleActivity extends AppCompatActivity implements  ArticleAdapter.OnSearchResultClickListener{
    private static final String TAG = DetailedArticleActivity.class.getSimpleName();
   // public static final String EXTRA_APPID = "DetailedArticleActivity.Int";
    private RecyclerView articleDetailRV;
    private ArticleAdapter articleAdapter;
    private ArticleViewModel viewModel;



private final DetailedArticleActivity lifecycleOwner = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_layout);

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
                } else if (item.getItemId() == R.id.trending_icon) {
                    Log.d(TAG, "Setting Activity to Trending");
                } else if (item.getItemId() == R.id.options_icon) {
                    Log.d(TAG, "Setting Activity to Options");
                }
                //else none of the id's match
                return false;
            }
        });
       this.viewModel = new ViewModelProvider(
               this,
               new ViewModelProvider.AndroidViewModelFactory(getApplication())

       ).get(ArticleViewModel.class);
       this.articleDetailRV = findViewById(R.id.rv_articles);
       this.articleDetailRV.setHasFixedSize(true);

       this.articleAdapter = new ArticleAdapter(this);
       this.articleDetailRV.setAdapter(articleAdapter);

      int tempAppId = getIntent().getIntExtra("EXTRA_APPID", 0);
        Log.d(TAG, "Here is the appid sent into the activity: " + tempAppId);
this.loadArticles(tempAppId);
//       this.viewModel.getArticleData().observe(
//               this,
//               new Observer<ArticleData>() {
//                   @Override
//                   public void onChanged(ArticleData articleData) {
//                       if(articleData != null) {
//                           articleAdapter.updateSearchResults(articleData.articleData);
//                           Log.d(TAG, "updating the articles");
//                       }
//
//                   }
//               }
//       );
    }


    @Override
    public void onSearchResultClicked(ArticleDataItem articleDataItem) {
        Log.d(TAG, "Opening the article in a browser: " + articleDataItem.title);
    }

    private void loadArticles(int appid){

        Log.d(TAG, "Loading articles for appid: "+ appid);



       this.viewModel.loadArticles(appid).observe(
               lifecycleOwner,
               new Observer<List<ArticleDataItem>>() {
                   @Override
                   public void onChanged(List<ArticleDataItem> articleDataItems) {
                       Log.d(TAG, "Updating the search results: " +articleDataItems);

                       articleAdapter.updateSearchResults(articleDataItems);
                       viewModel.loadArticles(appid).removeObserver(this);
                   }


               }
       );

    }
}

