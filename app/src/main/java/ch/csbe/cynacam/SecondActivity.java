package ch.csbe.cynacam;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static ch.csbe.cynacam.R.id.TextView05;
import static ch.csbe.cynacam.R.id.imageView2;
import static ch.csbe.cynacam.R.id.imageView3;


/**
 * Controller of activity_second.xml
 * @author Nana  Schütz & Cyrill Wagner
 * @version 1.0
 * @since   2017-03-22
 */
public class SecondActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private TextView locTextView;
    private LocationManager locationManager;
    private String provider;
    String cityname;
    ImageView picture;
    ImageView template;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        picture = (ImageView) findViewById(imageView2);
        picture.setImageBitmap(MainActivity.bitmap);
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

    /**
     * Puts templates picture into ImageView and changes if pressed on other template ImageView
     * @param view This represents activity_second
     * @return void
     */
    protected void template(View view){
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
            case(R.id.imageView7):
                template.setImageResource(R.drawable.template4);
                break;
            case(R.id.imageView8):
                template.setImageResource(R.drawable.template5);
                break;
            case(R.id.imageView9):
                template.setImageResource(R.drawable.template6);
                break;
        }
    }

    /**
     * Saves parameter bitmapImage to internal storage
     * @param bitmapImage This a Bitmap which is going to saved
     * @return String
     */
    private String saveToInternalStorage(Bitmap bitmapImage){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = Environment.getExternalStorageDirectory();
        File mypath = new File(directory.getAbsolutePath()+"/DCIM/Camera/img4.jpg");
        Log.d("Second", mypath.getAbsolutePath());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    /**
     * Overlays three Bitmaps(parameter)
     * @param bmap1 This is the first Bitmap of the overlay
     * @param bmap2 This is the second Bitmap of the overlay
     * @param bmap3 This is the third Bitmap of the overlay
     * @return Bitmap
     */
    private Bitmap overlay(Bitmap bmap1, Bitmap bmap2, Bitmap bmap3) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmap1.getWidth(), bmap1.getHeight(), bmap1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        Matrix m = new Matrix();
        canvas.drawBitmap(bmap1, m , null);
        Matrix m1 = new Matrix();
        Bitmap workingBitmap = Bitmap.createBitmap(bmap2);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.setHeight(bmap1.getHeight());
        mutableBitmap.setWidth(bmap1.getWidth());
        canvas.drawBitmap(mutableBitmap, m1, null);
        Matrix m2 = new Matrix();
        workingBitmap = Bitmap.createBitmap(bmap3);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.setHeight(50);
        mutableBitmap.setWidth(100);
        canvas.drawBitmap(mutableBitmap, m2, null);
        Log.d("overlay", "save");
        return bmOverlay;
    }

    /**
     * Onclick activity of save button, calls saveToInternalStorage method
     * @param view This represents activity_second
     * @return void
     */
    protected void save(View view){
        Log.d("Second", "save method");
        picture = (ImageView) findViewById(imageView2);
        template = (ImageView) findViewById(imageView3);
        text = (TextView) findViewById(TextView05);

        Bitmap immutableBmp = ((BitmapDrawable) picture.getDrawable()).getBitmap();
        Bitmap mutableBitmap = immutableBmp.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        canvas.drawBitmap(((BitmapDrawable) template.getDrawable()).getBitmap(),0,50,paint);

        Bitmap launcher = ((BitmapDrawable) template.getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        canvas.drawBitmap(launcher,0,10,paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText("Hallo Welt", 20,70, paint);

        picture.setImageBitmap(mutableBitmap);
        template.setImageBitmap(null);
        text.setText("");
    }
}

