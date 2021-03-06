package com.example.android.steamnews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.GameAppIdItem;

import java.util.List;

public class GameSearchActivity extends AppCompatActivity
    implements GameSearchAdapter.OnSearchResultClickListener {

    private static final String TAG = GameSearchActivity.class.getSimpleName();

    private RecyclerView searchResultsRV;
    private EditText searchBoxET;

    private GameSearchAdapter gameSearchAdapter;

    private GameAppIdViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        this.viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(GameAppIdViewModel.class);

        this.viewModel.loadAppList();

        this.searchBoxET = findViewById(R.id.et_game_search_box);
        this.searchResultsRV = findViewById(R.id.rv_game_search_results);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.gameSearchAdapter = new GameSearchAdapter(this);
        this.searchResultsRV.setAdapter(this.gameSearchAdapter);

        Button searchButton = findViewById(R.id.btn_game_search);

        final GameSearchActivity liveCycleContext = this;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBoxET.getText().toString();

                Log.d(TAG, "Executing search app list query: \"" + searchQuery + "\"");

                viewModel.searchAppList(searchQuery).observe(
                        liveCycleContext,
                        new Observer<List<GameAppIdItem>>() {
                            @Override
                            public void onChanged(List<GameAppIdItem> gameAppIdItems) {
                                Log.d(TAG, "Updating search results RV");
                                gameSearchAdapter.updateSearchResults(gameAppIdItems);
                                viewModel.searchAppList(searchQuery).removeObserver(this);
                            }
                        }
                );
            }
        });
    }

    @Override
    public void onSearchResultClicked(GameAppIdItem gameAppidItem) {
        gameAppidItem.bookmarked = !gameAppidItem.bookmarked;
        this.gameSearchAdapter.notifyDataSetChanged();
    }
}
