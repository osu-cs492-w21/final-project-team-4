package com.example.android.steamnews;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Settings extends AppCompatActivity {

    private Button submitb;
    EditText etusername, etsteamid;
    private ImageView imageView;
    private Toolbar menutoolbar;
    private Toolbar toptoolbar;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageView imgClick;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Capture the layout's TextView and set the string as its text
        TextView textView1 = findViewById(R.id.editText1);
        TextView textView2 = findViewById(R.id.editText2);
        //  Intent intent = getIntent();


        toptoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toptoolbar);
        getSupportActionBar().setIcon(R.drawable.steam_icon);


        menutoolbar = (Toolbar) findViewById(R.id.bottom_toolbar);
        menutoolbar.inflateMenu(R.menu.toolbar_menu_items);


        //Listeners for clicks on the buttons on the bottom of the screen.
        //These can be used to switch between activities
        menutoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Search");
                }else if(item.getItemId() == R.id.home_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Home");
                }else if(item.getItemId() == R.id.trending_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Trending");
                }else if(item.getItemId() == R.id.account_icon){
                    Log.d(Settings.class.getSimpleName(), "Setting Activity to Options");
                    openProfilePage();
                }
                //else none of the id's match
                return false;
            }

            public void openProfilePage() {
                Intent profileIntent = new Intent(Settings.this, Settings.class);
                //               EditText editText = (EditText) findViewById(R.id.editText);
                //         String message = editText.getText().toString();
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(profileIntent);
            }
        });



        imageView = (ImageView) findViewById(R.id.new_pic_iv);

        submitb = (Button) findViewById(R.id.submit_button);

        submitb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                etusername = (EditText) findViewById(R.id.editText1);
                etsteamid = (EditText) findViewById(R.id.editText2);
                // set the users input strings
                textView1.setText(etusername.getText().toString());
                textView2.setText(etsteamid.getText().toString());
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
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            imageView.setImageURI(image_uri);
        }
    }

}
