package com.example.android.steamnews;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.OnDatabaseActionCompleteCallback;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameSearchActivity extends AppCompatActivity
    implements GameSearchAdapter.OnSearchResultClickListener {

    private static final String TAG = GameSearchActivity.class.getSimpleName();

    private Toolbar menutoolbar;
    private Toolbar toptoolbar;

    private RecyclerView searchResultsRV;
    private EditText searchBoxET;
    private Button searchButton;

    private TextView errorMessageTV;
    private LinearLayout loadingIndicator;
    private TextView loadingIndicatorTV;

    private GameSearchAdapter gameSearchAdapter;
    private GameAppIdViewModel viewModel;

    private Toast warningToast;
    private Toast downloadCompleteToast;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        this.viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(GameAppIdViewModel.class);

        this.searchBoxET = findViewById(R.id.et_game_search_box);
        this.searchResultsRV = findViewById(R.id.rv_game_search_results);
        this.errorMessageTV = findViewById(R.id.tv_error_message);
        this.loadingIndicator = findViewById(R.id.ll_loading_indicator);
        this.loadingIndicatorTV = findViewById(R.id.tv_loading_indicator);
        this.menutoolbar = findViewById(R.id.bottom_toolbar);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.gameSearchAdapter = new GameSearchAdapter(this);
        this.searchResultsRV.setAdapter(this.gameSearchAdapter);

        // Set up search button in keyboard to perform a search
        this.searchBoxET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        // Set up search button in UI to perform a search
        this.searchButton = findViewById(R.id.btn_game_search);
        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // Check if the gameAppIdItems table is empty
        populateDatabase();

        setupMenuBar();
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.disposable.clear();
    }

    private void performSearch() {
        String searchQuery = searchBoxET.getText().toString().trim();
        if (TextUtils.isEmpty(searchQuery)) {
            if (warningToast != null) {
                warningToast.cancel();
            }
            warningToast = Toast.makeText(this, getString(R.string.game_search_empty_query_warning), Toast.LENGTH_SHORT);
            warningToast.show();
        } else {
            searchGameList(searchQuery);
        }
    }

    private void searchGameList(String searchQuery) {
        Log.d(TAG, "Executing search app list query: \"" + searchQuery + "\"");

        showLoadingIndicator(getString(R.string.game_search_loading_text_search));

        this.disposable.add(this.viewModel.searchAppList(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameAppIdItems -> {
                            Log.d(TAG, "Updating search results RV");
                            gameSearchAdapter.updateSearchResults(gameAppIdItems);
                            showSearchResults();
                        },
                        throwable -> Log.e(TAG, "Unable to perform search query", throwable)));

    }

    @Override
    public void onSearchResultClicked(GameAppIdItem gameAppidItem) {
        gameAppidItem.bookmarked = !gameAppidItem.bookmarked;

        this.viewModel.insertGameAppIdItem(gameAppidItem);

        this.gameSearchAdapter.notifyDataSetChanged();
    }

    private void setupMenuBar() {
        menutoolbar.inflateMenu(R.menu.toolbar_menu_items);

        //Listeners for clicks on the buttons on the bottom of the screen.
        //These can be used to switch between activities
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
                    Intent profileIntent = new Intent(GameSearchActivity.this, TrendingActivity.class);
                    startActivity(profileIntent);
                } else if (item.getItemId() == R.id.account_icon) {
                    Log.d(TAG, "Setting Activity to Options");
                    openProfilePage();
                }
                //else none of the id's match
                return false;
            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(GameSearchActivity.this, Settings.class);
                startActivity(profileIntent);
            }

            public void openHomePage() {
                Intent profileIntent = new Intent(GameSearchActivity.this, MainActivity.class);
                startActivity(profileIntent);
            }
        });
    }

    // Populate database if empty
    private void populateDatabase() {
        showLoadingIndicator(getString(R.string.game_search_loading_text_start));

        this.disposable.add(
                this.viewModel.countGameAppIdItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                rowCount -> {
                                    if (rowCount == 0) {
                                        Log.d(TAG, "Fetching app list for the first time");
                                        fetchAppList();
                                    } else {
                                        showSearchResults();
                                    }
                                },
                                throwable -> {
                                    Log.e(TAG, "Unable to populate database", throwable);
                                    showErrorMessage("Unable to populate database");
                                }
                        ));
    }

    // Fetch game appid list
    private void fetchAppList() {
        showLoadingIndicator(getString(R.string.game_search_loading_text_fetch));

        this.viewModel.fetchAppList(new OnDatabaseActionCompleteCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Finished fetching new items");

                runOnUiThread(() -> {
                    showSearchResults();
                    if (downloadCompleteToast != null) {
                        downloadCompleteToast.cancel();
                    }
                    downloadCompleteToast = Toast.makeText(GameSearchActivity.this, "Download complete", Toast.LENGTH_SHORT);
                    downloadCompleteToast.show();
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Unable to fetch app list", throwable);
                showErrorMessage(throwable.toString());
            }
        });
    }

    private void showSearchResults() {
        this.errorMessageTV.setVisibility(View.INVISIBLE);
        this.searchResultsRV.setVisibility(View.VISIBLE);
        this.loadingIndicator.setVisibility(View.INVISIBLE);
        this.searchButton.setEnabled(true);
    }

    private void showLoadingIndicator(String message) {
        errorMessageTV.setVisibility(View.INVISIBLE);
        searchResultsRV.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicatorTV.setText(message);
        searchButton.setEnabled(false);
    }

    private void showErrorMessage(String message) {
        errorMessageTV.setVisibility(View.VISIBLE);
        searchResultsRV.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        errorMessageTV.setText(getString(R.string.game_search_error_text, message));
        searchButton.setEnabled(false);
    }
}
