package com.example.android.steamnews;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class Settings extends AppCompatActivity {

    EditText usernameET, steamidET;
    private ImageView newImage;
    private ImageView addPicIcon;
    private Button submitb;

    private SharedPreferences userPreferences;

    // permission variables for camera
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.userPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ImageView imgClick;
        addPicIcon = findViewById(R.id.take_pic_iv);

        usernameET = (EditText)findViewById(R.id.username_edittext);
        steamidET = (EditText)findViewById(R.id.steamid_edittext);


        displayNewData();

        // bottom toolbar with icons
        Toolbar toptoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toptoolbar);
        getSupportActionBar().setIcon(R.drawable.steam_icon);

        Toolbar menutoolbar = (Toolbar) findViewById(R.id.bottom_toolbar);
        menutoolbar.inflateMenu(R.menu.toolbar_menu_items);

        //Listeners for clicks on the buttons on the bottom of the screen.
        //These can be used to switch between activities
        menutoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Search");
                    Intent profileIntent = new Intent(Settings.this, GameSearchActivity.class);
                    startActivity(profileIntent);
                }else if(item.getItemId() == R.id.home_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Home");
                    openHomePage();
                }else if(item.getItemId() == R.id.trending_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Trending");
                    Intent profileIntent = new Intent(Settings.this, TrendingActivity.class);
                    startActivity(profileIntent);
                }else if(item.getItemId() == R.id.account_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Options");
                    openProfilePage();
                }
                //else none of the id's match
                return false;
            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(Settings.this, Settings.class);
                startActivity(profileIntent);
            }
            public void openHomePage() {
                Intent profileIntent = new Intent(Settings.this, MainActivity.class);
                startActivity(profileIntent);
            }
        });

        newImage = (ImageView) findViewById(R.id.new_pic_iv);

        submitb = (Button) findViewById(R.id.submit_button);
        submitb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String userName = usernameET.getText().toString();
                String steamName = steamidET.getText().toString();

                SharedPreferences.Editor editor = userPreferences.edit();

                // for username
                editor.putString(getString(R.string.pref_user_username_key), userName);

                // for steam id
                editor.putString(getString(R.string.pref_user_steamid_key), steamName);

                editor.apply();
                // set the users new input strings
                displayNewData();
            }
        });


        imgClick = (ImageView)findViewById(R.id.take_pic_iv);
        imgClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });
    }

    private void displayNewData() {
        String userName = userPreferences.getString(getString(R.string.pref_user_username_key), "");

        String steamName =  userPreferences.getString(getString(R.string.pref_user_steamid_key), "");
        //set edittext to new data that user entered
        usernameET.setText(userName);
        steamidET.setText(steamName);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission from popup was granted
                openCamera();
            } else {
                //permission from popup was denied
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            newImage.setImageURI(image_uri);
            addPicIcon.setVisibility(View.GONE);
        }
    }

}
