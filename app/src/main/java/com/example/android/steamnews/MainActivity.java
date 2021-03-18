package com.example.android.steamnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.android.steamnews.data.GameAppIdItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
implements GameTitleAdapter.OnSearchResultClickListener{
    private final static String TAG = MainActivity.class.getSimpleName();

    /*
     * To use your own Steam API key, create a file called `gradle.properties` in your
     * GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
     * `$USER_HOME/.gradle/` in Windows), and add the following line:
     *
     *   STEAM_API_KEY="<put_your_own_Steam_API_key_here>"
     *
     * The Gradle build for this project is configured to automatically grab that value and store
     * it in the field `BuildConfig.STEAM_API_KEY` that's used below.  You can read more
     * about this setup on the following pages:
     *
     *   https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code
     *
     *   https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
     *
     * Alternatively, you can just hard-code your API key below 🤷‍.  If you do hard code your API
     * key below, make sure to get rid of the following line (line 18) in build.gradle:
     *
     *   buildConfigField("String", "STEAM_API_KEY", STEAM_API_KEY)
     *
     * Get your own Steam API key:
     *   https://steamcommunity.com/dev/apikey
     */
    private static final String STEAM_API_KEY = BuildConfig.STEAM_API_KEY;

    //rv for listing the titles of games
    private RecyclerView rvArticleView;
    private GameTitleAdapter titleAdapter;
    private GameAppIdViewModel viewModel;

    private SharedPreferences sharedPreferences;

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
                  Intent intent = new Intent(MainActivity.this, GameSearchActivity.class);
                  startActivity(intent);
              }else if(item.getItemId() == R.id.home_icon){
                  Log.d(TAG, "Setting Activity to Home");
              }else if(item.getItemId() == R.id.trending_icon){
                  Log.d(TAG, "Setting Activity to Trending");
                  Intent intent = new Intent(MainActivity.this, TrendingActivity.class);
                  startActivity(intent);
              }else if(item.getItemId() == R.id.account_icon){
                  Log.d(TAG, "Setting Activity to Options");
                  openProfilePage();
              }
              //else none of the id's match
                return false;
            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(MainActivity.this, Settings.class);
 //               EditText editText = (EditText) findViewById(R.id.editText);
       //         String message = editText.getText().toString();
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(profileIntent);
            }


        });
        this.rvArticleView=findViewById(R.id.rv_game_title);
        this.rvArticleView.setLayoutManager(new LinearLayoutManager(this));
        this.rvArticleView.setHasFixedSize(true);
        this.titleAdapter= new GameTitleAdapter(this);
        this.rvArticleView.setAdapter(this.titleAdapter);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
            case R.id.action_filter_news:
                openFilterNewsPopup();
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

    private void openFilterNewsPopup() {
        // Create popup window
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.filter_news_popup, null, false);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true); // Let taps outside the popup dismiss it

        String steamId = sharedPreferences.getString(
                getString(R.string.pref_user_steamid_key),
                null
        );

        if (steamId == null) {
            Log.w(TAG, "Steam ID is null");
        }

        TextView bookmarkedGamesFilter = popupView.findViewById(R.id.tv_filter_bookmarked_games);
        bookmarkedGamesFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Filter bookmarked games");
                popupWindow.dismiss();
            }
        });

        // Show popup window
        popupWindow.showAtLocation(findViewById(R.id.main_content), Gravity.CENTER, 0, 0);
    }
}
