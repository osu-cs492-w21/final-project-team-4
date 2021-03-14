package com.example.android.steamnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rvArticleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
}
