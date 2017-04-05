package com.example.hari.camerasample;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.hari.camerasample.R.id.imageView2;

import static com.example.hari.camerasample.R.id.imageView3;


/**
 * Created by cyrill.wagner on 24.03.2017.
 */


public class SecondActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private TextView locTextView; //Add a new TextView to your activity_main to display the address
    private LocationManager locationManager;
    private String provider;
    String cityname;
    ImageView test;
    ImageView template;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        test = (ImageView) findViewById(imageView2);
        test.setImageBitmap(MainActivity.bitmap);
        locTextView=(TextView)findViewById(R.id.TextView05);





        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        locationManager.getBestProvider(criteria, true);

        Location location=locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,true));

        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());
        Log.d("Tag","1");
        List<Address> addresses;

        try {
            addresses=gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses.size()>0)

            {
                cityname = addresses.get(0).getLocality();
                locTextView.setText(cityname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showImage(View view){
        test = (ImageView) findViewById(imageView3);
        test.setImageResource(R.drawable.template1);
        }

    protected void template(View view){
        test = (ImageView) findViewById(imageView3);
        template = (ImageView) findViewById(imageView3);

        switch (view.getId()){
            case(R.id.imageView4):
                template.setImageResource(R.drawable.template1);
                break;
            case(R.id.imageView5):
                template.setImageResource(R.drawable.template2);
                break;
            case(R.id.imageView6):
                template.setImageResource(R.drawable.template3);
                break;
        }
    }

    protected void save(){
        BitmapDrawable drawable1 = (BitmapDrawable) test.getDrawable();
        Bitmap bitmap1 = drawable1.getBitmap();
        BitmapDrawable drawable2 = (BitmapDrawable) template.getDrawable();
        Bitmap bitmap2 = drawable2.getBitmap();
        Bitmap bit = overlay(bitmap1, bitmap2);
        saveImageToInternalStorage(bit);

    }

    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
// Use the compress method on the Bitmap object to write image to
// the OutputStream
            FileOutputStream fos = SecondActivity.this.openFileOutput("desiredFilename.png", Context.MODE_PRIVATE);

// Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }


    private Bitmap overlay(Bitmap bmap1, Bitmap bmap2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmap1.getWidth(), bmap1.getHeight(), bmap1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmap1, new Matrix(), null);
        canvas.drawBitmap(bmap2, new Matrix(), null);
        return bmOverlay;
    }

}

