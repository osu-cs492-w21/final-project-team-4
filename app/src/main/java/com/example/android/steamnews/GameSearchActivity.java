package com.example.android.steamnews;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.GameAppidItem;

import java.util.ArrayList;

public class GameSearchActivity extends AppCompatActivity
    implements GameSearchAdapter.OnSearchResultClickListener {

    private static final String TAG = GameSearchActivity.class.getSimpleName();

    private RecyclerView searchResultsRV;
    private EditText searchBoxET;

    private GameSearchAdapter gameSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        this.searchBoxET = findViewById(R.id.et_game_search_box);
        this.searchResultsRV = findViewById(R.id.rv_game_search_results);

        this.searchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        this.searchResultsRV.setHasFixedSize(true);

        this.gameSearchAdapter = new GameSearchAdapter(this);
        this.searchResultsRV.setAdapter(this.gameSearchAdapter);

        // Example data // TODO remove
        ArrayList<GameAppidItem> exampleData = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            exampleData.add(new GameAppidItem("test game " + (i + 1), i, i % 3 == 0));
        }
        this.gameSearchAdapter.updateSearchResults(exampleData);

        Button searchButton = findViewById(R.id.btn_game_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    // TODO
                }
            }
        });
    }

    @Override
    public void onSearchResultClicked(GameAppidItem gameAppidItem) {
        gameAppidItem.bookmarked = !gameAppidItem.bookmarked;
        this.gameSearchAdapter.notifyDataSetChanged();
    }
}
