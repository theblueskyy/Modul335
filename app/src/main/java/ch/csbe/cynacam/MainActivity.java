package ch.csbe.cynacam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Controller of activity_main.xml
 * @author Nana  Schütz & Cyrill Wagner
 * @version 1.0
 * @since   2017-03-22
 */
public class MainActivity extends AppCompatActivity {

    ImageView result;
    public static Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button click = (Button)findViewById(R.id.camera);
    }

    /**
     * Opens Phone's Gallery
     * @param view This represents activity_main
     * @return void
     */
    protected void showImages(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        intent.setType("image/*");
        startActivity(intent);
    }

    /**
     * Opens Phone's Camera
     * @param view This represents activity_main
     * @return void
     */
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Intent menuIntent = new Intent(this, SecondActivity.class);
            startActivity(menuIntent);
        }
    }
}
